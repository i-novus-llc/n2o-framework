package net.n2oapp.framework.engine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
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
            for (String forReplace : literalsForReplacers.keySet()) {
                String tmp = par.replace(forReplace, literalsForReplacers.get(forReplace));
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
        Collections.sort(res, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length()) return 1;
                else if (o1.length() > o2.length()) return -1;
                else return 0;
            }
        });
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
            int skipToPosition = i;
            while (i < statement.length) {
                skipToPosition = skipCommentsAndQuotes(statement, i);
                if (i == skipToPosition) {
                    break;
                } else {
                    i = skipToPosition;
                }
            }
            if (i >= statement.length) {
                break;
            }
            char c = statement[i];
            if (c == ':' || c == '&') {
                int j = i + 1;
                if (j < statement.length && statement[j] == ':' && c == ':') {
                    // Postgres-style "::" casting operator - to be skipped.
                    i = i + 2;
                    continue;
                }
                String parameter = null;
                if (j < statement.length && c == ':' && statement[j] == '{') {
                    // :{x} style parameter
                    while (j < statement.length && !('}' == statement[j])) {
                        j++;
                        if (':' == statement[j] || '{' == statement[j]) {
                            throw new IllegalStateException("Parameter name contains invalid character '" +
                                    statement[j] + "' at position " + i + " in statement: " + sql);
                        }
                    }
                    if (j >= statement.length) {
                        throw new IllegalStateException(
                                "Non-terminated named IllegalStateException declaration at position " + i + " in statement: " + sql);
                    }
                    if (j - i > 3) {
                        parameter = sql.substring(i + 2, j);
                        namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
                        totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes, i, j + 1, parameter);
                    }
                    j++;
                } else {
                    while (j < statement.length && !isParameterSeparator(statement[j])) {
                        j++;
                    }
                    if (j - i > 1) {
                        parameter = sql.substring(i + 1, j);
                        namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
                        totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes, i, j, parameter);
                    }
                }
                i = j - 1;
            } else {
                if (c == '\\') {
                    int j = i + 1;
                    if (j < statement.length && statement[j] == ':') {
                        // this is an escaped : and should be skipped
                        sqlToUse = sqlToUse.substring(0, i - escapes) + sqlToUse.substring(i - escapes + 1);
                        escapes++;
                        i = i + 2;
                        continue;
                    }
                }
                if (c == '?') {
                    unnamedParameterCount++;
                    totalParameterCount++;
                }
            }
            i++;
        }
        ParsedSql parsedSql = new ParsedSql(sqlToUse);
        for (ParameterHolder ph : parameterList) {
            parsedSql.addNamedParameter(ph.getParameterName(), ph.getStartIndex(), ph.getEndIndex());
        }
        parsedSql.setNamedParameterCount(namedParameterCount);
        parsedSql.setUnnamedParameterCount(unnamedParameterCount);
        parsedSql.setTotalParameterCount(totalParameterCount);
        return parsedSql;
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
            if (statement[position] == START_SKIP[i].charAt(0)) {
                boolean match = true;
                for (int j = 1; j < START_SKIP[i].length(); j++) {
                    if (!(statement[position + j] == START_SKIP[i].charAt(j))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    int offset = START_SKIP[i].length();
                    for (int m = position + offset; m < statement.length; m++) {
                        if (statement[m] == STOP_SKIP[i].charAt(0)) {
                            boolean endMatch = true;
                            int endPos = m;
                            for (int n = 1; n < STOP_SKIP[i].length(); n++) {
                                if (m + n >= statement.length) {
                                    // last comment not closed properly
                                    return statement.length;
                                }
                                if (!(statement[m + n] == STOP_SKIP[i].charAt(n))) {
                                    endMatch = false;
                                    break;
                                }
                                endPos = m + n;
                            }
                            if (endMatch) {
                                // found character sequence ending comment or quote
                                return endPos + 1;
                            }
                        }
                    }
                    // character sequence ending comment or quote not found
                    return statement.length;
                }

            }
        }
        return position;
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

        private List<String> parameterNames = new ArrayList<String>();

        private List<int[]> parameterIndexes = new ArrayList<int[]>();

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

