package net.n2oapp.framework.api.script;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;

import javax.script.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static net.n2oapp.framework.api.StringUtils.*;

/**
 * Утилитный класс для генерации js скриптов
 */
public class ScriptProcessor {
    private static final List<String> momentFuncs = Arrays.asList("moment", "now", "today", "yesterday", "tomorrow",
            "beginWeek", "endWeek", "beginMonth", "endMonth", "beginQuarter", "endQuarter", "beginYear", "endYear");
    private static final String SPREAD_OPERATOR = "*.";

    private static final ScriptEngineManager engineMgr = new ScriptEngineManager();
    private static volatile ScriptEngine scriptEngine;
    private static String momentJs;

    private static final Pattern FUNCTION_PATTERN = Pattern.compile("function\\s*\\(\\)[\\s\\S]*");
    private static final Pattern TERNARY_IN_LINK_PATTERN = Pattern.compile(".*\\{.*\\?.*:.*}.*");
    private static final Pattern FUNCTION_CONTENT_PATTERN = Pattern.compile("\\b(return|if|var|switch|const|let|for|while)\\b");

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
                return toJsExpression(expr.replace("#<", "#{").replace("$<", "${").replace(">>", "}"));
            } else {
                return expr.replace("#<", "#{").replace("$<", "${").replace(">>", "}");
            }
        }
        return text;
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
        String trimmedText = removeSpaces(text);
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
        String[] split = text.split("\\{");
        if (split.length <= 1)
            return text;
        if (!split[0].equals("")) {
            sb.append("'".concat(split[0]).concat("'"));
        }
        for (int i = 1; i < split.length; i++) {
            int idxSuffix = split[i].indexOf("}");
            if (idxSuffix > 0) {
                String value = split[i].substring(0, idxSuffix);
                if (value.contains(SPREAD_OPERATOR)) {
                    value = value.substring(0, value.indexOf(SPREAD_OPERATOR)) + ".map(function(t){return t." +
                            value.substring(value.indexOf(SPREAD_OPERATOR) + 2) + "})";
                }
                sb.append("+").append(value);
                if (idxSuffix < split[i].length() - 1) {
                    String afterValue = split[i].substring(idxSuffix + 1);
                    sb.append("+'").append(afterValue).append("'");
                }
            }
        }
        String res = sb.toString();
        if (res.startsWith("+")) {
            res = res.replaceFirst("\\+", "");
        }
        if (res.endsWith("+")) {
            res = res.substring(0, res.length() - 1);
        }
        return res;
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
                || (n2oSwitch.getValueFieldId() == null || n2oSwitch.getValueFieldId().length() == 0))
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
        res.append(" || (" + variable + " === null)");
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
        String str = "new Date(%s.replace(/(\\d{2})\\.(\\d{2})\\.(\\d{4})/,'$3-$2-$1')).getTime()";
        str = String.format(str, variable);
        exp.append(str);
        exp.append(" < ");
        String s = "new Date('%s').getTime()";
        exp.append(String.format(s, new SimpleDateFormat("MM.dd.yyyy HH:mm:SS").format(date)));
        return exp.toString();
    }

    public String buildMoreExpression(String variable, Comparable comparable) {
        if (comparable instanceof Date date)
            return buildMoreExpressionForDate(variable, date);
        return variable + " > " + comparable;
    }

    private String buildMoreExpressionForDate(String variable, Date date) {
        StringBuilder exp = new StringBuilder();
        String str = "new Date(%s.replace(/(\\d{2})\\.(\\d{2})\\.(\\d{4})/,'$3-$2-$1')).getTime()";
        str = String.format(str, variable);
        exp.append(str);
        exp.append(" > ");
        String s = "new Date('%s').getTime()";
        exp.append(String.format(s, new SimpleDateFormat("MM.dd.yyyy HH:mm:SS").format(date)));
        return exp.toString();
    }

    public String buildInIntervalExpression(String variable, Interval interval) {
        if (interval.getDomain().equals(Date.class)) return buildInDateIntervalExpression(variable,
                (Interval<Date>) interval);
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
        String str = "new Date(%s.replace(/(\\d{2})\\.(\\d{2})\\.(\\d{4})/,'$3-$2-$1')).getTime()";
        str = String.format(str, variable);
        exp.append(str);
        exp.append(" > ");
        String s = "new Date('%s').getTime()";
        exp.append(String.format(s, new SimpleDateFormat("MM.dd.yyyy HH:mm:SS").format(interval.getBegin())));
        exp.append(" && ");
        exp.append(str);
        exp.append(" < ");
        exp.append(String.format(s, new SimpleDateFormat("MM.dd.yyyy HH:mm:SS").format(interval.getEnd())));
        return exp.toString();
    }


    public static String buildAddConjunctionCondition(String condition, String addedCondition) {
        StringBuilder exp = new StringBuilder();
        exp.append('(').append(condition).append(')').append(" && ").append('(').append(addedCondition).append(')');
        return exp.toString();
    }

    public static Set<String> extractVars(String script) {
        final Set<String> names = new HashSet<>();
        class Visitor implements NodeVisitor {
            @Override
            public boolean visit(AstNode node) {
                if (node instanceof PropertyGet) {
                    names.add(node.toSource());
                    return false;
                }
                if (node instanceof Name) {
                    names.add(node.toSource());
                }
                return true;
            }
        }
        AstNode node = null;
        try {
            node = new Parser().parse(script, null, 1);
        } catch (EvaluatorException e) {
            throw new ScriptParserException(script, e);
        }
        node.visit(new Visitor());
        return names;
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
        class Visitor implements NodeVisitor {
            @Override
            public boolean visit(AstNode node) {
                if (node instanceof PropertyGet left) {
                    while (left.getLeft() instanceof PropertyGet propertyGet) {
                        left = propertyGet;
                    }
                    if (!predicate.test(left.getTarget().getString())) return false;
                    left.setLeft(new PropertyGet(new Name(1, context), (Name) left.getLeft()));
                    return false;
                }

                if (node instanceof Name name) {
                    if (predicate.test(node.getString())) {
                        name.setIdentifier(context + '.' + name.getIdentifier());
                    }
                    return false;
                }
                return true;
            }
        }
        AstNode node = new Parser().parse(script, null, 1);
        node.visit(new Visitor());
        return node.toSource();
    }

    public static String addContextFor(String script, final String context, final Collection<String> vars) {
        return addContextFor(script, context, vars::contains);
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
        ScriptEngine scriptEngine = getScriptEngine();
        Bindings global = scriptEngine.getContext().getBindings(ScriptContext.GLOBAL_SCOPE);
        Bindings bindings = scriptEngine.createBindings();
        bindings.putAll(global);
        for (Map.Entry<String, Object> entry : dataSet.entrySet()) {
            Object value = entry.getValue();
            bindings.put(entry.getKey(),
                    value instanceof Collection collection ?
                            collection.toArray() :
                            value);
        }
        if (isNeedMoment(script)) {
            scriptEngine.eval(momentJs, bindings);
        }
        return (T) scriptEngine.eval(script, bindings);
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
        if (scriptEngine == null) {
            createScriptEngine();
        }
        return scriptEngine;
    }

    private static synchronized void createScriptEngine() {
        if (scriptEngine == null) {
            scriptEngine = engineMgr.getEngineByName("JavaScript");
            if (scriptEngine == null)
                throw new N2oException("ScriptEngine 'JavaScript' not found in classpath");
            Bindings bindings = scriptEngine.createBindings();
            URL momentUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/moment.js");
            URL lodashUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/lodash.js");
            URL numeralUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/numeral.js");
            URL n2oUrl = ScriptProcessor.class.getClassLoader().getResource("META-INF/resources/js/n2o.js");
            try {
                momentJs = IOUtils.toString(momentUrl, StandardCharsets.UTF_8);
                scriptEngine.eval(IOUtils.toString(lodashUrl, StandardCharsets.UTF_8), bindings);
                scriptEngine.eval(IOUtils.toString(numeralUrl, StandardCharsets.UTF_8), bindings);
                scriptEngine.eval(IOUtils.toString(n2oUrl, StandardCharsets.UTF_8), bindings);
            } catch (IOException | ScriptException e) {
                throw new N2oException(e);
            }
            scriptEngine.getContext().setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
        }
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
                resultValue = resultValue.substring(1, resultValue.length() - 1);
            } else
                resultValue = "'" + entry.getValue() + "'";
            result.put(resultKey, resultValue);
        }
        return result;
    }


}
