package net.n2oapp.framework.engine.data.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.BinaryOperator;

import static net.n2oapp.framework.engine.data.QueryUtil.normalizeQueryParams;
import static net.n2oapp.framework.engine.data.QueryUtil.replaceListPlaceholder;


/**
 * Сервис вызова Spring RestTemplate
 */
public class SpringRestDataProviderEngine implements MapInvocationEngine<N2oRestDataProvider> {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String baseRestUrl;

    public SpringRestDataProviderEngine(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void setBaseRestUrl(String baseRestUrl) {
        this.baseRestUrl = baseRestUrl;
    }

    @Override
    public Object invoke(N2oRestDataProvider invocation, Map<String, Object> data) {
        String query = invocation.getQuery();
        if (query == null)
            throw new N2oException("query mustn't be null");
        final N2oRestDataProvider.Method method = invocation.getMethod() == null ? N2oRestDataProvider.Method.GET : invocation.getMethod();
        Map<String, Object> args = new HashMap<>();
        data.forEach(args::put);
        if (!query.contains("http")) {
            if (!query.startsWith("/"))
                query = "/" + query;
            if (query.contains("//"))
                query = query.replace("//", "/");
            query = baseRestUrl + query;
        }

        query = replaceListPlaceholder(query, "{select}", args.remove("select"), "", (a, b) -> a + invocation.getSelectSeparator() + b);
        query = replaceListPlaceholder(query,
                "{filters}",
                args.remove("filters"), "",
                (str) -> resolve(str, args, (a, b) -> a + invocation.getFiltersSeparator() + b),
                (a, b) -> a + invocation.getFiltersSeparator() + b);
        query = replaceListPlaceholder(query, "{sorting}", args.remove("sorting"), "", (a, b) -> a + invocation.getSortingSeparator() + b);
        query = replaceListPlaceholder(query, "{join}", args.remove("join"), "", (a, b) -> a + invocation.getJoinSeparator() + b);
        query = resolvePathPlaceholders(query, args);
        query = normalizeQueryParams(query);
        return executeQuery(method.name(), query, args, invocation.getProxyHost(), invocation.getProxyPort());
    }

    /**
     * Инициализация заголовков запроса
     *
     * @param args Аргументы запроса
     * @return Заголовки
     */
    protected HttpHeaders initHeaders(Map<String, Object> args) {
        return new HttpHeaders();
    }

    private Object executeQuery(String method, String query, Map<String, Object> args, String proxyHost,
                                Integer proxyPort) {
        query = getURL(proxyHost, proxyPort, query);
        HttpHeaders headers = initHeaders(args);
        Map<String, Object> body = new HashMap<>(args);

        switch (method) {
            case "GET": {
                return restTemplate.exchange(query, HttpMethod.GET, new HttpEntity<>(headers), DataSet.class).getBody();
            }
            case "POST": {
                return restTemplate.exchange(query, HttpMethod.POST, new HttpEntity<>(body, headers), DataSet.class).getBody();
            }
            case "PUT": {
                return restTemplate.exchange(query, HttpMethod.PUT, new HttpEntity<>(body, headers), DataSet.class).getBody();
            }
            case "DELETE": {
                return restTemplate.exchange(query, HttpMethod.DELETE, new HttpEntity<>(headers), DataSet.class).getBody();
            }
            default:
                throw new UnsupportedOperationException("Method " + method + " unsupported");
        }
    }

    private String resolvePathPlaceholders(String query, Map<String, Object> args) {
        for (String key : new HashSet<>(args.keySet())) {
            String p = "{" + key + "}";
            if (query.contains(p)) {
                query = replacePlaceholder(query, key, args.get(key) == null ? "" : args.get(key));
                args.remove(key);
            }
        }
        return query;
    }

    private String resolve(String str, Map<String, Object> args, BinaryOperator<String> reducer) {
        if (!str.contains("{") || !str.contains("}")) return str;
        String paramKey = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
        if (!(args.get(paramKey) instanceof List)) {
            return replacePlaceholder(str, paramKey, args.get(paramKey));
        }
        Optional<String> result = ((List<String>) args.get(paramKey))
                .stream()
                .map(item -> replacePlaceholder(str, paramKey, item))
                .reduce(reducer);
        return result.orElse("");
    }

    private String replacePlaceholder(String target, String key, Object value) {
        try {
            return target.replace(
                    "{" + key + "}",
                    encode(objectMapper.writeValueAsString(value).replace("\"", ""))
            );
        } catch (JsonProcessingException e) {
            throw new N2oException(e);
        }
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private static String getURL(String host, Integer port, String url) {
        if (host == null || port == null)
            return url;
        else
            return "http://" + host + ":" + port + url;
    }

    @Override
    public Class<? extends N2oRestDataProvider> getType() {
        return N2oRestDataProvider.class;
    }

}
