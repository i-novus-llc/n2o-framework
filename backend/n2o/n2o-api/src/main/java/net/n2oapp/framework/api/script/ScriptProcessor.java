package net.n2oapp.framework.api.script;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import org.apache.commons.io.IOUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static net.n2oapp.framework.api.StringUtils.hasLink;
import static net.n2oapp.framework.api.StringUtils.isJs;

/**
 * Утилитный класс для генерации js скриптов
 */
public class ScriptProcessor {
    private static final List<String> momentFuncs = Arrays.asList("moment", "now", "today", "yesterday", "tomorrow",
            "beginWeek", "endWeek", "beginMonth", "endMonth", "beginQuarter", "endQuarter", "beginYear", "endYear");
    private static final String SPREAD_OPERATOR = "*.";
    private static final String SPREAD_TO_MAP_TEMPLATE = ".map(function(t){return t.";

    private static final HostAccess HOST_ACCESS = HostAccess.newBuilder()
            .allowArrayAccess(true)
            .allowListAccess(true)
            .allowMapAccess(true)
            .build();
    private static final Engine GRAAL_ENGINE = Engine.newBuilder()
            .option("engine.WarnInterpreterOnly", "false")
            .build();
    private static final int POOL_SIZE = Math.max(3, Runtime.getRuntime().availableProcessors() * 2);
    private static final BlockingQueue<ScriptEngine> ENGINE_POOL = new ArrayBlockingQueue<>(POOL_SIZE);
    private static final AtomicBoolean POOL_INITIALIZED = new AtomicBoolean(false);
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static volatile String momentJs;

    private static final Pattern FUNCTION_PATTERN = Pattern.compile("function\\s*\\(\\)[\\s\\S]*");
    private static final Pattern TERNARY_IN_LINK_PATTERN = Pattern.compile(".*(?<!#)\\{(?!.*#\\{).*\\?.*:.*}.*");
    private static final Pattern FUNCTION_CONTENT_PATTERN = Pattern.compile("\\b(return|if|var|switch|const|let|for|while)\\b");
    private static final String EUROPEAN_DATE_TO_TIMESTAMP_TEMPLATE = "new Date(%s.replace(/(\\d{2})\\.(\\d{2})\\.(\\d{4})/,'$3-$2-$1')).getTime()";
    private static final String ISO_DATE_TO_TIMESTAMP_TEMPLATE = "new Date('%s').getTime()";
    private static final String TIMESTAMP_FORMAT = "MM.dd.yyyy HH:mm:SS";
    private static final String N2O_SCRIPT_VAR = "__n2oScript";

    public static String resolveLinks(String text) {
        if (text == null)
            return null;
        if (hasLink(text)) {
            String expr = text;
            if (TERNARY_IN_LINK_PATTERN.matcher(expr).matches()) {
                expr = expr.replace("{", "{(").replace("}", ")}");
            }
            if (expr.contains("${")) {
                while (expr.contains("${")) {
                    int idx = expr.indexOf("}", expr.indexOf("${"));
                    boolean notEnd = idx < expr.length() - 1;
                    expr = expr.replaceFirst("\\$\\{", "\\$<");
                    if (notEnd) {
                        expr = expr.substring(0, idx) + ">>" + expr.substring(idx + 1);
                    } else {
                        expr = expr.substring(0, idx) + ">>";
                    }

                }
            }
            if (expr.contains("#{")) {
                while (expr.contains("#{")) {
                    int idx = expr.indexOf("}", expr.indexOf("#{"));
                    boolean notEnd = idx < expr.length() - 1;
                    expr = expr.replaceFirst("#\\{", "#<");
                    if (notEnd) {
                        expr = expr.substring(0, idx) + ">>" + expr.substring(idx + 1);
                    } else {
                        expr = expr.substring(0, idx) + ">>";
                    }
                }
            }
            if (hasLink(expr)) {
                expr = resolveToJsString(expr);
                return toJsExpression(restorePlaceholders(expr));
            } else {
                if (text.startsWith("{") && text.endsWith("}"))
                    return toJsExpression(restorePlaceholders(expr));
                return restorePlaceholders(expr);
            }
        }
        return text;
    }

    private static String restorePlaceholders(String expr) {
        return expr.replace("#<", "#{").replace("$<", "${").replace(">>", "}");
    }

    /**
     * Преобразование выражения в самовызывающуюся js функцию.
     * Примеры.
     * "if (gender.id = 1) return 'М'; else return 'Ж';" -> "(function(){if (gender.id = 1) return 'М'; else return 'Ж';})()"
     * "gender.id == 1" -> "gender.id == 1"
     * "function(){if (gender.id = 1) return 'М'; else return 'Ж';}" -> "(function(){if (gender.id = 1) return 'М'; else return 'Ж';})()"
     * "(function(){ return '123'; })()" -> "(function(){ return '123'; })()"
     *
     * @param text выражение сождержащее ссылки
     * @return js функция
     */
    public static String resolveFunction(String text) {
        if (text == null)
            return null;
        String trimmedText = text.trim();
        if (trimmedText.startsWith("(function")) {
            return text;
        }
        if (FUNCTION_PATTERN.matcher(trimmedText).matches()) {
            return String.format("(%s).call(this)", trimmedText);
        }
        if (isFunction(trimmedText)) {
            return String.format("(function(){%s}).call(this)", trimmedText);
        }
        if (trimmedText.endsWith(";")) {
            trimmedText = trimmedText.substring(0, trimmedText.length() - 1);
        }
        return trimmedText;
    }

    private static boolean isFunction(String text) {
        if (text.startsWith("{") && text.endsWith("}"))
            text = text.substring(1, text.length() - 1);
        if (text.contains("*."))
            return false;
        String wordReplaced = text.replaceAll("'[^']*'", "");
        String bracesReplaced = wordReplaced.replaceAll("\\{[^{}]*}", "");
        return FUNCTION_CONTENT_PATTERN.matcher(bracesReplaced).find();
    }


    /**
     * Преобразование выражений с ссылками в js код.
     * Примеры.
     * {id} -> `id` (String), "1" -> 1 (Integer)
     * "true" -> true (Boolean), "1,2" -> 1,2 (String)
     * "test" -> test (String), "`1+1`" -> `1+1` (String)
     * "Test{id}" -> `'Test'+id` (String)
     * #{test} -> #{test} (String), ${test} -> ${test} (String)
     * {test*.id} -> `.map(fuct...)`
     *
     * @param text выражение, содержащее ссылки
     * @return js код
     */
    public static Object resolveExpression(String text) {
        String expression = resolveLinks(text);
        if (expression == null)
            return null;
        if (expression.equals("true") || expression.equals("false"))
            return Boolean.valueOf(expression);
        if (expression.matches("([\\d]+)")) {
            try {
                return Integer.parseInt(expression);
            } catch (NumberFormatException e) {
            }
        }
        return expression;
    }

    /**
     * Изменить значение JS выражения на обратное
     *
     * @param text JS выражение или текст
     * @return Обратное JS выражение или объект
     */
    public static Object invertExpression(String text) {
        Object result = resolveExpression(text);
        if (result == null)
            return null;
        if (result instanceof Boolean bool)
            return !bool;
        if (!isJs(result))
            return result;
        String expr = (String) result;
        expr = expr.substring(1, expr.length() - 1);
        expr = toJsExpression("!(" + expr + ")");
        return expr;
    }

    /**
     * Преобразование списка expression в js код
     * Примеры
     * "1" -> [1] (List<Integer>), ("1", "2") -> [1,2] (List<Integer>)
     * ("Test1", "Test2") -> ["Test1","Test2"] (List<String>)
     * ("true", "false") -> [true,false] (List<Boolean>)
     * ("{id}") -> `[id]` (String), ("{id1}", "{id2}") -> `[id1,id2]` (String)
     * ("Test{id1}", "Test{id2}") -> `['Test'+id1,'Test'+id2]` (String)
     *
     * @param values значения, записанное в xml как список value
     * @return js код
     */
    public static Object resolveArrayExpression(String... values) {
        //todo реализовать варианты с плейсхлодарами(пока реализовано только для констант)
        if (values.length == 0) {
            return null;
        } else {
            List result = new ArrayList();
            for (String value : values) {
                result.add(resolveExpression(value));
            }
            return result;
        }
    }

    private static String toJsExpression(String expression) {
        return "`" + expression + "`";
    }

    public static Object removeJsBraces(Object expression) {
        if (expression instanceof String str)
            return str.replace("`", "");
        return expression;
    }

    private static String resolveToJsString(String text) {
        if (text == null) return null;
        StringBuilder sb = new StringBuilder();

        int pos = 0;
        boolean first = true;

        while (pos < text.length()) {
            int openBrace = text.indexOf('{', pos);
            if (openBrace == -1) {
                String remaining = text.substring(pos);
                if (!remaining.isEmpty()) {
                    if (first) {
                        sb.append("'").append(remaining).append("'");
                    } else {
                        sb.append("+'").append(remaining).append("'");
                    }
                }
                break;
            }

            if (openBrace > pos) {
                String before = text.substring(pos, openBrace);
                if (first) {
                    sb.append("'").append(before).append("'");
                    first = false;
                } else {
                    sb.append("+'").append(before).append("'");
                }
            }

            int closeBrace = findMatchingBrace(text, openBrace);
            if (closeBrace == -1) {
                String rest = text.substring(openBrace);
                if (first) {
                    sb.append("'").append(rest).append("'");
                } else {
                    sb.append("+'").append(rest).append("'");
                }
                break;
            }

            // Извлекаем содержимое между скобками (исходное, до обработки spread)
            String rawInner = text.substring(openBrace + 1, closeBrace);

            // Обрабатываем spread-оператор
            String inner = processSpreadOperatorInExpression(rawInner);

            // Если это функция, оборачиваем в самовызывающуюся функцию
            if (isFunction("{" + rawInner + "}")) {
                inner = String.format("(function(){%s}).call(this)", inner);
            }

            if (first) {
                sb.append(inner);
                first = false;
            } else {
                sb.append("+").append(inner);
            }

            pos = closeBrace + 1;
        }

        return sb.toString();
    }

    /**
     * Находит позицию закрывающей скобки с учетом вложенности
     */
    private static int findMatchingBrace(String text, int openPos) {
        int braceCount = 1;
        boolean inString = false;
        char stringChar = 0;

        for (int i = openPos + 1; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\'' || c == '"') {
                if (!inString) {
                    inString = true;
                    stringChar = c;
                } else if (c == stringChar && text.charAt(i - 1) != '\\') {
                    inString = false;
                }
                continue;
            }

            if (!inString) {
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Обрабатывает spread-оператор (*.) в JavaScript выражении, заменяя его на вызов .map().
     * Spread-оператор {@code *.} преобразуется в шаблон {@code .map(function(t){return t.}}.
     * Метод корректно обрабатывает выражения как с тернарными операторами, так и без них.
     *
     * @param value JavaScript выражение, которое может содержать spread-операторы
     */
    private static String processSpreadOperatorInExpression(String value) {
        int spreadPos = value.indexOf(SPREAD_OPERATOR);

        if (spreadPos == -1)
            return value;

        if (!(value.contains("?") && value.contains(":")))
            return value.substring(0, spreadPos) + SPREAD_TO_MAP_TEMPLATE +
                   value.substring(spreadPos + 2) + "})";

        // Если выражение содержит тернарный оператор
        StringBuilder result = new StringBuilder();
        int start = 0;
        while (spreadPos != -1) {
            // Находим конец свойства после spread оператора
            String after = value.substring(spreadPos + SPREAD_OPERATOR.length());
            int propEnd = findEndOfProperty(after);
            result.append(value, start, spreadPos) // Добавляем часть до spread оператора
                    .append(SPREAD_TO_MAP_TEMPLATE) // Добавляем преобразованный spread оператор
                    .append(after, 0, propEnd).append("})");

            start = spreadPos + SPREAD_OPERATOR.length() + propEnd;
            spreadPos = value.indexOf(SPREAD_OPERATOR, start);
        }
        // Добавляем оставшуюся часть
        if (start < value.length())
            result.append(value.substring(start));
        return result.toString();
    }

    /**
     * Находит позицию конца идентификатора в строке.
     * Метод анализирует строку и определяет позицию, на которой заканчивается идентификатор
     *
     * @param str строка для анализа
     */
    private static int findEndOfProperty(String str) {
        // Находим конец идентификатора
        int bracketCount = 0;
        char bracketType = 0;
        int i = 0;
        while (i < str.length()) {
            char c = str.charAt(i);
            if (c == '[' || c == '(') {
                if (bracketCount == 0)
                    bracketType = (c == '[') ? ']' : ')';
                bracketCount++;
            } else if (bracketCount > 0 && c == bracketType) {
                bracketCount--;
                if (bracketCount == 0)
                    bracketType = 0;
                i++;
                continue; // Продолжаем, если внутри скобок
            }
            // Если не внутри скобок и символ не является частью идентификатора
            if (bracketCount == 0 && !Character.isJavaIdentifierPart(c))
                return i;
            i++;
        }
        return str.length();
    }

    public static String createFunctionCall(String funcName, Object... args) {
        StringBuilder res = new StringBuilder();
        res.append(funcName);
        res.append('(');
        boolean begin = true;
        for (Object arg : args) {
            if (!begin) res.append(",");
            //добавляем кавычки для строк
            String str = arg instanceof String argString ? "'" + argString + "'" : arg.toString();
            res.append(str);
            begin = false;
        }
        res.append(')');
        return res.toString();
    }

    public static String createSelfInvokingFunction(String code) {
        return "function(){" + code + "}()";
    }

    public static String buildSwitchExpression(N2oSwitch n2oSwitch) {
        if (n2oSwitch == null
            || (n2oSwitch.getCases() == null && n2oSwitch.getResolvedCases() == null && n2oSwitch.getDefaultCase() == null)
            || (n2oSwitch.getValueFieldId() == null || n2oSwitch.getValueFieldId().isEmpty()))
            return null;
        Map<Object, String> cases = resolveSwitchCases(n2oSwitch.getResolvedCases() != null ? n2oSwitch.getResolvedCases() : n2oSwitch.getCases());
        StringBuilder b = new StringBuilder("`");
        for (Map.Entry<Object, String> entry : cases.entrySet()) {
            b.append(n2oSwitch.getValueFieldId())
                    .append(" == ")
                    .append(entry.getKey())
                    .append(" ? ")
                    .append(entry.getValue())
                    .append(" : ");
        }
        if (n2oSwitch.getDefaultCase() != null) {
            b.append("'")
                    .append(ScriptProcessor.resolveExpression(n2oSwitch.getDefaultCase()))
                    .append("'");
        } else {
            b.append("null");
        }
        return b.append("`").toString();
    }


    public String buildIsNullExpression(String variable) {
        StringBuilder res = null;
        StringBuilder tmpVar = null;
        for (String s : variable.split("\\.")) {
            if (tmpVar == null)
                tmpVar = new StringBuilder();
            else
                tmpVar.append(".");
            tmpVar.append(s);
            if (res == null)
                res = new StringBuilder();
            else
                res.append(" || ");
            res.append(buildUndefinedExpression(tmpVar.toString()));
        }
        if (res == null)
            return null;
        res.append(" || (").append(variable).append(" === null)");
        return res.toString();
    }


    public String buildIsNotNullExpression(String variable) {
        return variable + " != null";
    }


    private String buildUndefinedExpression(String variable) {
        return "(typeof " + variable + " === 'undefined')";
    }

    public String buildEqualExpression(String variable, Object value) {
        return variable + " == " + getString(value);
    }

    public String buildLikeExpression(String variable, String value) {
        String res = "%s.indexOf(%s) !== -1";
        return String.format(res, variable, getString(value));
    }

    public String buildLikeStartExpression(String variable, String value) {
        String res = "%s.indexOf(%s) === 0";
        return String.format(res, variable, getString(value));
    }

    public String buildInListExpression(String variable, List<Object> values) {
        String res = "_.indexOf(_.isArray(%s) ? %s : [%s], %s) >= 0";
        String array = values.stream().map(this::getString).toList().toString();
        return String.format(res, array, array, array, variable);
    }

    public String buildOverlapListExpression(String variable, List<Object> values) {
        String res = "_.intersection(_.isArray(%s) ? %s : [%s], %s).length > 0";
        return String.format(res, variable, variable, variable, values.stream().map(this::getString).toList().toString());
    }

    public String buildContainsListExpression(String variable, List<Object> values) {
        String res = "_.intersection(_.isArray(%s) ? %s : [%s], %s).length === (%s).length";
        String vals = values.stream().map(this::getString).toList().toString();
        return String.format(res, variable, variable, variable, vals, vals);
    }

    public String buildNotInListExpression(String variable, List<Object> values) {
        return "!(" + buildInListExpression(variable, values) + ")";
    }

    public String buildLessExpression(String variable, Comparable comparable) {
        if (comparable instanceof Date date) return buildLessExpressionForDate(variable, date);
        return variable + " < " + comparable;
    }

    public String buildNotEqExpression(String variable, Object value) {
        return variable + " != " + getString(value);
    }

    private String buildLessExpressionForDate(String variable, Date date) {
        StringBuilder exp = new StringBuilder();
        String str = String.format(EUROPEAN_DATE_TO_TIMESTAMP_TEMPLATE, variable);
        exp.append(str);
        exp.append(" < ");
        String s = ISO_DATE_TO_TIMESTAMP_TEMPLATE;
        exp.append(String.format(s, new SimpleDateFormat(TIMESTAMP_FORMAT).format(date)));
        return exp.toString();
    }

    public String buildMoreExpression(String variable, Comparable comparable) {
        if (comparable instanceof Date date)
            return buildMoreExpressionForDate(variable, date);
        return variable + " > " + comparable;
    }

    private String buildMoreExpressionForDate(String variable, Date date) {
        StringBuilder exp = new StringBuilder();
        String str = String.format(EUROPEAN_DATE_TO_TIMESTAMP_TEMPLATE, variable);
        exp.append(str);
        exp.append(" > ");
        String s = ISO_DATE_TO_TIMESTAMP_TEMPLATE;
        exp.append(String.format(s, new SimpleDateFormat(TIMESTAMP_FORMAT).format(date)));
        return exp.toString();
    }

    public String buildInIntervalExpression(String variable, Interval interval) {
        if (interval.getDomain().equals(Date.class)) return buildInDateIntervalExpression(variable, interval);
        StringBuilder exp = new StringBuilder();
        exp.append(variable);
        exp.append(" > ");
        exp.append(interval.getBegin());
        exp.append(" && ");
        exp.append(variable);
        exp.append(" < ");
        exp.append(interval.getEnd());
        return exp.toString();
    }


    private String buildInDateIntervalExpression(String variable, Interval<Date> interval) {
        StringBuilder exp = new StringBuilder();
        String str = String.format(EUROPEAN_DATE_TO_TIMESTAMP_TEMPLATE, variable);
        exp.append(str);
        exp.append(" > ");
        String s = ISO_DATE_TO_TIMESTAMP_TEMPLATE;
        exp.append(String.format(s, new SimpleDateFormat(TIMESTAMP_FORMAT).format(interval.getBegin())));
        exp.append(" && ");
        exp.append(str);
        exp.append(" < ");
        exp.append(String.format(s, new SimpleDateFormat(TIMESTAMP_FORMAT).format(interval.getEnd())));
        return exp.toString();
    }


    public static String buildAddConjunctionCondition(String condition, String addedCondition) {
        StringBuilder exp = new StringBuilder();
        exp.append('(').append(condition).append(')').append(" && ").append('(').append(addedCondition).append(')');
        return exp.toString();
    }

    public static Set<String> extractVars(String script) {
        if (script == null) return new HashSet<>();
        ScriptEngine engine = getScriptEngine();
        try {
            Bindings b = engine.createBindings();
            b.put(N2O_SCRIPT_VAR, script);
            String json = (String) engine.eval("JSON.stringify(n2oExtractVars(" + N2O_SCRIPT_VAR + "))", b);
            if (json == null || json.equals("null") || json.equals("[]")) return new HashSet<>();
            String[] arr = JSON_MAPPER.readValue(json, String[].class);
            return new HashSet<>(Arrays.asList(arr));
        } catch (ScriptException e) {
            throw new ScriptParserException(script, e);
        } catch (Exception e) {
            throw new N2oException(e);
        } finally {
            releaseScriptEngine(engine);
        }
    }

    private static Set<String> extractRootIdentifiers(String script) {
        if (script == null) return Set.of();
        ScriptEngine engine = getScriptEngine();
        try {
            Bindings b = engine.createBindings();
            b.put(N2O_SCRIPT_VAR, script);
            String json = (String) engine.eval("JSON.stringify(n2oExtractRootVars(" + N2O_SCRIPT_VAR + "))", b);
            if (json == null || json.equals("null") || json.equals("[]")) return Set.of();
            String[] arr = JSON_MAPPER.readValue(json, String[].class);
            return new HashSet<>(Arrays.asList(arr));
        } catch (ScriptException e) {
            throw new ScriptParserException(script, e);
        } catch (Exception e) {
            throw new N2oException(e);
        } finally {
            releaseScriptEngine(engine);
        }
    }

    public static Map<String, Set<String>> extractPropertiesOf(String script, final Collection<String> vars) {
        final Map<String, Set<String>> result = new LinkedHashMap<>();
        final Set<String> names = extractVars(script);
        for (String name : names) {
            for (String str : vars) {
                String prefix = str + ".";
                int idx = name.indexOf(prefix);
                if (idx >= 0) {
                    Set<String> properties = result.computeIfAbsent(str, k -> new LinkedHashSet<>());
                    String afterGet = name.substring(idx + prefix.length());
                    int endIdx = afterGet.indexOf(".");
                    properties.add(endIdx >= 0 ? afterGet.substring(0, endIdx) : afterGet);
                }
            }
        }
        return result;
    }


    public static String addContextFor(String script, final String context, Predicate<String> predicate) {
        Set<String> allRoots = extractRootIdentifiers(script);
        List<String> filtered = allRoots.stream().filter(predicate).toList();
        if (filtered.isEmpty()) return script;
        return addContextFor(script, context, filtered);
    }

    public static String addContextFor(String script, final String context, final Collection<String> vars) {
        if (vars == null || vars.isEmpty()) return script;
        ScriptEngine engine = getScriptEngine();
        try {
            String varsJson = JSON_MAPPER.writeValueAsString(vars);
            Bindings b = engine.createBindings();
            b.put(N2O_SCRIPT_VAR, script);
            b.put("__n2oContext", context);
            b.put("__n2oVarsJson", varsJson);
            return (String) engine.eval("n2oAddContextFor(" + N2O_SCRIPT_VAR + ", __n2oContext, JSON.parse(__n2oVarsJson))", b);
        } catch (Exception e) {
            throw new N2oException(e);
        } finally {
            releaseScriptEngine(engine);
        }
    }

    public static String addContextForAll(String script, final String context) {
        return addContextFor(script, context, s -> true);
    }

    //    'id, gender[0].id' to 'id, gender'
    public static String simplifyArrayLinks(String src) {
        StringBuilder sb = new StringBuilder();
        Set<String> buffer = new HashSet<>();
        boolean begin = true;
        for (String tmp : src.split("\\,")) {
            String[] s = tmp.split("\\[");
            if (s.length > 1) tmp = s[0];
            else {
                s = tmp.split("\\*");
                if (s.length > 1) tmp = s[0];
            }
            if (!buffer.contains(tmp)) {
                if (!begin) sb.append(',');
                begin = false;
                buffer.add(tmp);
                sb.append(tmp);
            }
        }
        return sb.toString();
    }


    @SuppressWarnings("unchecked")
    public static <T> T eval(String script, DataSet dataSet) throws ScriptException {
        ScriptEngine engine = getScriptEngine();
        try {
            return doEval(script, dataSet, engine);
        } finally {
            releaseScriptEngine(engine);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T doEval(String script, DataSet dataSet, ScriptEngine engine) throws ScriptException {
        Bindings global = engine.getContext().getBindings(ScriptContext.GLOBAL_SCOPE);
        Bindings bindings = engine.createBindings();
        bindings.putAll(global);
        // Intermediate map to collect nested structures before binding
        Map<String, Object> nestedAccumulator = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : dataSet.entrySet()) {
            Object value = entry.getValue();
            Object normalizedValue = value instanceof Collection collection ? collection.toArray() : value;
            String key = entry.getKey();
            if (key.contains(".")) {
                putNestedInto(nestedAccumulator, key, normalizedValue);
            } else {
                bindings.put(key, normalizedValue);
                nestedAccumulator.put(key, normalizedValue);
            }
        }
        // Inject nested structures as native JS objects via JSON eval
        for (Map.Entry<String, Object> entry : nestedAccumulator.entrySet()) {
            Object val = entry.getValue();
            boolean needsJsonInjection = val instanceof Map
                    || (val != null && val.getClass().isArray() && val.getClass().getComponentType().isPrimitive());
            if (needsJsonInjection) {
                try {
                    String json = JSON_MAPPER.writeValueAsString(val);
                    engine.eval("var " + entry.getKey() + " = " + json + ";", bindings);
                } catch (Exception e) {
                    bindings.put(entry.getKey(), val);
                }
            }
        }
        if (isNeedMoment(script)) {
            engine.eval(momentJs, bindings);
        }
        return (T) engine.eval(script, bindings);
    }

    @SuppressWarnings("unchecked")
    private static void putNestedInto(Map<String, Object> map, String key, Object value) {
        String[] parts = key.split("\\.", 2);
        String root = parts[0];
        if (parts.length == 1) {
            map.put(root, value);
        } else {
            Object existing = map.get(root);
            Map<String, Object> nested;
            if (existing instanceof Map) {
                nested = (Map<String, Object>) existing;
            } else {
                nested = new LinkedHashMap<>();
                map.put(root, nested);
            }
            putNestedInto(nested, parts[1], value);
        }
    }

    public static boolean evalForBoolean(String script, DataSet dataSet) {
        try {
            Object eval = eval(script, dataSet);
            if (eval == null) return false;
            if (eval instanceof Boolean bool)
                return bool;
            return true;
        } catch (ScriptException e) {
            return false;
        }
    }


    public static String ifNotUndefined(String exp, String... fields) {
        StringBuilder res = new StringBuilder();
        for (String field : retrieve(fields)) {
            res.append("(typeof ");
            res.append(field);
            res.append(" === 'undefined')");
            res.append(" || ");
        }
        res.append('(');
        res.append(exp);
        res.append(')');
        return res.toString();
    }

    private static List<String> retrieve(String[] fields) {
        List<String> res = new ArrayList<>();
        for (String field : fields) {
            res.addAll(retrieve(field));
        }
        return res;
    }


    private static List<String> retrieve(String field) {
        List<String> res = new ArrayList<>();
        String[] tmp = field.split("\\.");
        for (int i = 0; i < tmp.length; i++) {
            res.add(toString(Arrays.copyOfRange(tmp, 0, i + 1)));
        }
        return res;
    }

    private static String toString(String[] array) {
        StringBuilder res = new StringBuilder();
        boolean begin = true;
        for (int i = 0; i < array.length; i++) {
            if (!begin) res.append('.');
            res.append(array[i]);
            begin = false;
        }
        return res.toString();
    }

    public static ScriptEngine getScriptEngine() {
        ensurePoolInitialized();
        try {
            ScriptEngine engine = ENGINE_POOL.poll(30, TimeUnit.SECONDS);
            if (engine == null) {
                throw new N2oException("Script engine pool exhausted: no engine available after 30 seconds");
            }
            return engine;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new N2oException(e);
        }
    }

    public static void releaseScriptEngine(ScriptEngine engine) {
        ENGINE_POOL.offer(engine);
    }

    private static void ensurePoolInitialized() {
        if (!POOL_INITIALIZED.get()) {
            initializePool();
        }
    }

    private static synchronized void initializePool() {
        if (POOL_INITIALIZED.get()) return;
        for (int i = 0; i < POOL_SIZE; i++) {
            ENGINE_POOL.offer(createNewEngine());
        }
        POOL_INITIALIZED.set(true);
    }

    private static ScriptEngine createNewEngine() {
        ScriptEngine engine = GraalJSScriptEngine.create(GRAAL_ENGINE,
                Context.newBuilder("js").allowHostAccess(HOST_ACCESS));
        Bindings bindings = engine.createBindings();
        URL momentUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/moment.js");
        URL lodashUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/lodash.js");
        URL numeralUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/numeral.js");
        URL n2oUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/n2o.js");
        URL acornUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/acorn.js");
        URL astUtilsUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/n2o-ast-utils.js");
        try {
            if (momentJs == null) {
                momentJs = IOUtils.toString(momentUrl, StandardCharsets.UTF_8);
            }
            engine.eval(IOUtils.toString(lodashUrl, StandardCharsets.UTF_8), bindings);
            engine.eval(IOUtils.toString(numeralUrl, StandardCharsets.UTF_8), bindings);
            engine.eval(IOUtils.toString(n2oUrl, StandardCharsets.UTF_8), bindings);
            engine.eval(IOUtils.toString(acornUrl, StandardCharsets.UTF_8), bindings);
            engine.eval(IOUtils.toString(astUtilsUrl, StandardCharsets.UTF_8), bindings);
        } catch (IOException | ScriptException e) {
            throw new N2oException(e);
        }
        engine.getContext().setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
        return engine;
    }

    private String getString(Object value) {
        if (value instanceof String str)
            return "'" + str + "'";
        else if (value instanceof Date dateValue)
            return "'" + new SimpleDateFormat(DomainProcessor.JAVA_DATE_FORMAT).format(dateValue) + "'";
        return value.toString();
    }


    private static boolean isNeedMoment(String script) {
        return momentFuncs.stream().anyMatch(script::contains);
    }

    private static Map<Object, String> resolveSwitchCases(Map<?, String> cases) {
        Map<Object, String> result = new HashMap<>();
        for (Map.Entry<?, String> entry : cases.entrySet()) {
            Object resultKey = entry.getKey();
            if (resultKey instanceof String)
                resultKey = "'" + resultKey + "'";
            String resultValue;
            if (hasLink(entry.getValue())) {
                resultValue = ScriptProcessor.resolveLinks(entry.getValue());
                resultValue = resultValue == null ? null : resultValue.substring(1, resultValue.length() - 1);
            } else
                resultValue = "'" + entry.getValue() + "'";
            result.put(resultKey, resultValue);
        }
        return result;
    }
}
