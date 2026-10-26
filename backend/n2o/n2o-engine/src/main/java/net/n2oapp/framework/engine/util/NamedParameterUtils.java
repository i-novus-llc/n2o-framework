package net.n2oapp.framework.engine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

import static java.util.Comparator.comparingInt;

/**
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NamedParameterUtils {
    private static final HashMap<String, String> LITERALS_FOR_REPLACE;

    static {
        LITERALS_FOR_REPLACE = new HashMap<>();
        LITERALS_FOR_REPLACE.put("*", "[]");
    }

    private static final char[] PARAMETER_SEPARATORS =
            new char[]{'"', '\'', ':', '&', ',', ';', '(', ')', '|', '=', '+', '-', '%', '/', '\\', '<', '>', '^'};
    private static final String[] START_SKIP =
            new String[]{"'", "\"", "--", "/*"};
    private static final String[] STOP_SKIP =
            new String[]{"'", "\"", "\n", "*/"};

    public static List<String> parseNamedParameters(String string) {
        return parseSqlStatement(string).getParameterNames();
    }


    /**
     * происходит замена литералов которые спринг не понимает, как в тексте запроса, так и в самом тексте
     * плюс все null-овые аргументы заменяются на null. Сделано это из-за того что постгрес ругается на выражения типа
     * ':id is null' если id = null.
     *
     * @param query                запрос с именованными параметрами
     * @param args                 аргументы запроса
     * @param literalsForReplacers литералы которые мы хотим заменить (те которые спринг не понимает)
     * @return преобразованную выборку и аргументы
     */
    public static QueryBlank prepareQuery(String query, Map<String, Object> args, Map<String, String> literalsForReplacers) {
        final Map<String, Object> finalArgs = new HashMap<>(args);

        //заменяем плохие литералы
        for (String par : new ArrayList<>(finalArgs.keySet())) {
            for (Map.Entry<String, String> entry : literalsForReplacers.entrySet()) {
                String tmp = par.replace(entry.getKey(), entry.getValue());
                if (!tmp.equals(par)) {
                    finalArgs.put(tmp, finalArgs.get(par));
                    finalArgs.remove(par);
                    query = query.replace(":" + par, ":" + tmp);
                    par = tmp;
                }
            }
        }

        //заменяем null
        int shift = 0;
        ParsedSql parsedSql = parseSqlStatement(query);
        for (int i = 0; i < parsedSql.getParameterNames().size(); i++) {
            String paramName = parsedSql.getParameterNames().get(i);
            if (finalArgs.get(paramName) == null) {
                int begIdx = parsedSql.getParameterIndexes(i)[0] + shift;
                int endIdx = parsedSql.getParameterIndexes(i)[1] + shift;
                //подставляя null вместо плейсхолдера мы нарушаем индексацию. Поэтому акуммулируем "сдвиг"
                shift += 3 - paramName.length();
                query = query.substring(0, begIdx) + "null" + query.substring(endIdx);
            }
        }

        QueryBlank res = new QueryBlank();
        res.setArgs(finalArgs);
        res.setQuery(query);
        return res;
    }

    public static QueryBlank prepareQuery(String query, Map<String, Object> args) {
        return prepareQuery(query, args, LITERALS_FOR_REPLACE);
    }

    public static List<String> sortByLength(List<String> parameterList) {
        ArrayList<String> res = new ArrayList<>(parameterList);
        res.sort(comparingInt(String::length).reversed());
        return res;
    }

    //копипаст со спринга (просто у него все приватно)
    private static ParsedSql parseSqlStatement(final String sql) {
        Set<String> namedParameters = new HashSet<>();
        String sqlToUse = sql;
        List<ParameterHolder> parameterList = new ArrayList<>();
        char[] statement = sql.toCharArray();

        int namedParameterCount = 0;
        int unnamedParameterCount = 0;
        int totalParameterCount = 0;
        int escapes = 0;

        int i = 0;
        while (i < statement.length) {
            i = skipToNonSkippable(statement, i);
            if (i >= statement.length)
                break;

            char c = statement[i];
            if (c == ':' || c == '&') {
                ParsedParameter pp = parseParameter(statement, sql, i);
                if (pp.hasName()) {
                    namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, pp.name());
                    totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes,
                            pp.start(), pp.end(), pp.name());
                }
                i = pp.nextIndex();
            } else if (isEscapedColon(statement, i)) {
                sqlToUse = removeEscapingBackslash(sqlToUse, i, escapes);
                escapes++;
                i += 2;
            } else {
                if (c == '?') {
                    unnamedParameterCount++;
                    totalParameterCount++;
                }
                i++;
            }
        }

        ParsedSql parsedSql = new ParsedSql(sqlToUse);
        for (ParameterHolder ph : parameterList)
            parsedSql.addNamedParameter(ph.getParameterName(), ph.getStartIndex(), ph.getEndIndex());
        parsedSql.setNamedParameterCount(namedParameterCount);
        parsedSql.setUnnamedParameterCount(unnamedParameterCount);
        parsedSql.setTotalParameterCount(totalParameterCount);
        return parsedSql;
    }

    /**
     * Продвинуться вперёд, пропуская комментарии и строковые литералы,
     * пока не встретится позиция с «настоящим» текстом запроса.
     */
    private static int skipToNonSkippable(char[] statement, int i) {
        while (i < statement.length) {
            int skipTo = skipCommentsAndQuotes(statement, i);
            if (i == skipTo)
                break;
            i = skipTo;
        }
        return i;
    }

    /**
     * Разобрать именованный параметр: {@code :name}, {@code :{name}}, {@code ::}, {@code &name}.
     * Возвращает новый {@code i} и, если параметр найден, его имя и границы.
     */
    private static ParsedParameter parseParameter(char[] statement, String sql, int i) {
        int j = i + 1;
        if (j < statement.length && statement[j] == ':' && statement[i] == ':')
            return ParsedParameter.skipped(i + 2);
        if (j < statement.length && statement[i] == ':' && statement[j] == '{')
            return parseBracedParameter(statement, sql, i, j);
        return parseSimpleParameter(statement, sql, i, j);
    }

    private static ParsedParameter parseBracedParameter(char[] statement, String sql, int i, int j) {
        while (j < statement.length && '}' != statement[j]) {
            j++;
            if (j < statement.length && (':' == statement[j] || '{' == statement[j]))
                throw new IllegalStateException(String.format(
                        "Имя параметра содержит недопустимый символ '%s' в позиции %d в запросе: %s",
                        statement[j], j, sql));
        }
        if (j >= statement.length)
            throw new IllegalStateException(String.format(
                    "Незакрытое объявление именованного параметра в позиции %d в запросе: %s",
                    i, sql));
        boolean hasName = j - i > 3;
        String name = hasName ? sql.substring(i + 2, j) : null;
        int end = hasName ? j + 1 : 0;
        return new ParsedParameter(j + 1, name, i, end);
    }

    private static ParsedParameter parseSimpleParameter(char[] statement, String sql, int i, int j) {
        while (j < statement.length && !isParameterSeparator(statement[j]))
            j++;
        boolean hasName = j - i > 1;
        String name = hasName ? sql.substring(i + 1, j) : null;
        int end = hasName ? j : 0;
        return new ParsedParameter(j, name, i, end);
    }

    private static boolean isEscapedColon(char[] statement, int i) {
        return statement[i] == '\\' && i + 1 < statement.length && statement[i + 1] == ':';
    }

    private static String removeEscapingBackslash(String sql, int i, int escapes) {
        return sql.substring(0, i - escapes) + sql.substring(i - escapes + 1);
    }

    /**
     * Результат разбора одного именованного параметра: новый {@code i}, имя и границы
     * (start/end для {@link ParameterHolder}), если параметр найден.
     */
    private record ParsedParameter(int nextIndex, String name, int start, int end) {
        static ParsedParameter skipped(int nextIndex) {
            return new ParsedParameter(nextIndex, null, 0, 0);
        }

        boolean hasName() {
            return name != null;
        }
    }

    private static int addNamedParameter(
            List<ParameterHolder> parameterList, int totalParameterCount, int escapes, int i, int j, String parameter) {

        parameterList.add(new ParameterHolder(parameter, i - escapes, j - escapes));
        totalParameterCount++;
        return totalParameterCount;
    }

    private static int addNewNamedParameter(Set<String> namedParameters, int namedParameterCount, String parameter) {
        if (!namedParameters.contains(parameter)) {
            namedParameters.add(parameter);
            namedParameterCount++;
        }
        return namedParameterCount;
    }

    private static int skipCommentsAndQuotes(char[] statement, int position) {
        for (int i = 0; i < START_SKIP.length; i++) {
            if (matchesAt(statement, position, START_SKIP[i]))
                return skipUntilStop(statement, position + START_SKIP[i].length(), STOP_SKIP[i]);
        }
        return position;
    }

    /**
     * Совпадает ли {@code pattern} с участком массива начиная с {@code position}.
     * Возвращает {@code false}, если до конца массива символов не хватает.
     */
    private static boolean matchesAt(char[] statement, int position, String pattern) {
        if (position + pattern.length() > statement.length)
            return false;
        for (int j = 0; j < pattern.length(); j++) {
            if (statement[position + j] != pattern.charAt(j))
                return false;
        }
        return true;
    }

    /**
     * Ищет закрывающую последовательность {@code stop}, начиная с {@code from}.
     * Если не нашлась — возвращает длину массива.
     */
    private static int skipUntilStop(char[] statement, int from, String stop) {
        for (int m = from; m < statement.length; m++) {
            if (matchesAt(statement, m, stop))
                return m + stop.length();
        }
        return statement.length;
    }

    private static boolean isParameterSeparator(char c) {
        if (Character.isWhitespace(c)) {
            return true;
        }
        for (char separator : PARAMETER_SEPARATORS) {
            if (c == separator) {
                return true;
            }
        }
        return false;
    }

    private static class ParameterHolder {
        private final String parameterName;
        private final int startIndex;
        private final int endIndex;

        public ParameterHolder(String parameterName, int startIndex, int endIndex) {
            this.parameterName = parameterName;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public String getParameterName() {
            return this.parameterName;
        }

        public int getStartIndex() {
            return this.startIndex;
        }

        public int getEndIndex() {
            return this.endIndex;
        }
    }

    private static class ParsedSql {

        private String originalSql;

        private List<String> parameterNames = new ArrayList<>();

        private List<int[]> parameterIndexes = new ArrayList<>();

        private int namedParameterCount;

        private int unnamedParameterCount;

        private int totalParameterCount;

        public ParsedSql(String originalSql) {
            this.originalSql = originalSql;
        }

        public String getOriginalSql() {
            return this.originalSql;
        }

        public void addNamedParameter(String parameterName, int startIndex, int endIndex) {
            this.parameterNames.add(parameterName);
            this.parameterIndexes.add(new int[]{startIndex, endIndex});
        }

        public List<String> getParameterNames() {
            return this.parameterNames;
        }

        public int[] getParameterIndexes(int parameterPosition) {
            return this.parameterIndexes.get(parameterPosition);
        }

        public void setNamedParameterCount(int namedParameterCount) {
            this.namedParameterCount = namedParameterCount;
        }

        public int getNamedParameterCount() {
            return this.namedParameterCount;
        }

        public void setUnnamedParameterCount(int unnamedParameterCount) {
            this.unnamedParameterCount = unnamedParameterCount;
        }

        public int getUnnamedParameterCount() {
            return this.unnamedParameterCount;
        }

        public void setTotalParameterCount(int totalParameterCount) {
            this.totalParameterCount = totalParameterCount;
        }

        public int getTotalParameterCount() {
            return this.totalParameterCount;
        }

        @Override
        public String toString() {
            return this.originalSql;
        }

    }
}