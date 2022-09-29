package net.n2oapp.framework.engine.data;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

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

    /**
     * Копирование заголовков из запроса клиента
     *
     * @param forwardedHeaders Заголовки которые надо скопировать
     * @param headers          Заголовки запроса к сервису
     */
    public static void copyForwardedHeaders(Set<String> forwardedHeaders, HttpHeaders headers) {
        if (isEmpty(forwardedHeaders))
            return;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (String forwardedHeaderName : forwardedHeaders) {
            if ("*".equals(forwardedHeaderName)) {
                copyForwardedHeaders(new HashSet<>(Collections.list(request.getHeaderNames())), headers);
            }
            String forwardedHeaderValue = request.getHeader(forwardedHeaderName);
            if (hasText(forwardedHeaderValue))
                headers.add(forwardedHeaderName, forwardedHeaderValue);
        }
    }

    /**
     * Копирование Cookie из запроса клиента
     *
     * @param forwardedCookies Cookie которые надо скопировать
     * @param headers          Заголовки запроса к сервису
     */
    public static void copyForwardedCookies(Set<String> forwardedCookies, HttpHeaders headers) {
        if (isEmpty(forwardedCookies))
            return;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringJoiner cookieJoiner = new StringJoiner(";");
        for (String forwardedCookieName : forwardedCookies) {
            if (nonNull(request.getCookies())) {
                if ("*".equals(forwardedCookieName)) {
                    Arrays.stream(request.getCookies())
                            .map(cookie -> String.format("%s=%s", cookie.getName(), cookie.getValue())).forEach(cookieJoiner::add);
                    break;
                }
                Arrays.stream(request.getCookies())
                        .filter(cookie -> forwardedCookieName.equals(cookie.getName()))
                        .findFirst().map(cookie -> String.format("%s=%s", cookie.getName(), cookie.getValue()))
                        .ifPresent(cookieJoiner::add);
            }
        }
        if (cookieJoiner.length() > 0)
            headers.add(HttpHeaders.COOKIE, cookieJoiner.toString());
    }

    public static Set<String> parseHeadersString(String headers) {
        if (!hasText(headers))
            return null;
        if (headers.contains("*"))
            return Set.of("*");
        Set<String> result = new HashSet<>();
        for (String forwardedHeaderName : headers.trim().split(",")) {
            forwardedHeaderName = forwardedHeaderName.trim();
            if (hasText(forwardedHeaderName))
                result.add(forwardedHeaderName);
        }
        return result;
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
            clause = ((List<String>) list).stream().map(resolver).reduce(reducer).orElse(defaultValue);
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

    public static List<String> insertPrefixSuffix(List<String> args, String prefix, String suffix) {
        return args.stream().map(s -> prefix + s + suffix).collect(Collectors.toList());
    }
}
