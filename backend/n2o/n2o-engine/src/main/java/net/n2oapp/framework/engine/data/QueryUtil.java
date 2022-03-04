package net.n2oapp.framework.engine.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Собирает данные для вызова InvocationEngine
 */
public abstract class QueryUtil {
    private static final String AMP_ESCAPE = "&amp;";

    public static String normalizeQueryParams(String query) {
        String q = query;
        if (q.contains(AMP_ESCAPE))
            q = q.replace(AMP_ESCAPE, "&");
        if (q.contains("?")) {
            //query params
            q = q.replaceAll("&+", "&");
            q = q.replace("?&", "?");
            if (q.endsWith("?"))
                q = q.substring(0, q.length() - 1);
        }
        return q;
    }

    public static String replacePlaceholders(String baseQuery, Predicate<String> matcher, Function<String, Object> resolver) {
        String[] tokens = baseQuery.split("[ ,&;=?\n]");
        List<String> placeholders = new ArrayList<>();
        for (String token : tokens) {
            if (matcher.test(token))
                placeholders.add(token);
        }
        for (String placeholder : placeholders) {
            baseQuery = replacePlaceholder(baseQuery, placeholder, resolver.apply(placeholder), placeholder);
        }
        return baseQuery;
    }

    public static String replacePlaceholder(String baseQuery, String placeholder, Object value, String defaultValue) {
        if (!baseQuery.contains(placeholder)) return baseQuery;
        String clause = defaultValue;
        if (value != null) {
            clause = value.toString();
        }
        return baseQuery.replace(placeholder, clause);
    }

    public static String replaceListPlaceholder(String baseQuery, String placeholder, Object list, String defaultValue,
                                                Function<String, String> resolver,
                                                BinaryOperator<String> reducer) {
        if (!baseQuery.contains(placeholder)) return baseQuery;
        String clause = defaultValue;
        if (list != null) {
            clause = ((List<String>)list).stream().map(resolver).reduce(reducer).orElse(defaultValue);
        }
        return baseQuery.replace(placeholder, clause);
    }

    public static String replaceListPlaceholder(String baseQuery, String placeholder, Object list,
                                                String defaultValue, BinaryOperator<String> reducer) {
        return replaceListPlaceholder(baseQuery, placeholder, list, defaultValue, Function.identity(), reducer);
    }

    public static String reduceAnd(String a, String b) {
        return reduceSeparator(a, b, " AND ");
    }

    public static String reduceComma(String a, String b) {
        return reduceSeparator(a, b, ", ");
    }

    public static String reduceSpace(String a, String b) {
        return reduceSeparator(a, b, " ");
    }

    public static String reduceSeparator(String a, String b, String separator) {
        return a + separator + b;
    }
}
