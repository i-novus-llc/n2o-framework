package net.n2oapp.framework.boot.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.n2oapp.framework.api.JsonUtil;
import net.n2oapp.framework.api.exception.N2oException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: operehod
 * Date: 03.03.2015
 * Time: 16:16
 */
public class InvocationUtil {

    private static final Pattern SQL_ERROR_PATTERN = Pattern.compile("(\\A|\n)[A-Z][a-z](.|\n)+?; SQL statement:");

    private InvocationUtil() {
    }

    /**
     * Прокапываем ошибку до конца
     */
    public static String findSqlSummary(Throwable e) {

        if (e == null)
            return null;

        if (e instanceof SQLException && ((SQLException) e).getSQLState().equals("P0001"))
            return e.getMessage();

        return findSqlSummary(e.getCause());
    }

    public static String constructSqlMessage(DataAccessException e) {
        String sqlMessage = e.getMessage();
        if (e instanceof BadSqlGrammarException)
            sqlMessage = ((BadSqlGrammarException) e).getSQLException().getMessage();
        Matcher matcher = SQL_ERROR_PATTERN.matcher(sqlMessage);
        if (matcher.find())
            return "Bad SQL grammar: " + (matcher.group().startsWith("\n")  ?
                    StringUtils.substringBetween(matcher.group(), "\n", "; SQL statement:")
                    : StringUtils.substringBefore(matcher.group(), "; SQL statement:"));
        return sqlMessage;
    }

    public static void mapAndListsToJson(Map<String, Object> args) {
        for (String key : new HashSet<>(args.keySet())) {
            Object value = args.get(key);
            args.put(key, resolveValue(value));
        }
    }

    public static void mapAndListsToJson(Object[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                args[i] = resolveValue(args[i]);
            }
        }
    }

    private static Object resolveValue(Object value) {
        if (value instanceof Map || value instanceof List)
            try {
                value = JsonUtil.getMapper().writeValueAsString(value);
            } catch (JsonProcessingException e) {
                throw new N2oException(e);
            }
        return value;
    }

}
