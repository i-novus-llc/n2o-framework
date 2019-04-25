package net.n2oapp.framework.engine.data.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
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
    private ResponseExtractor<Object> responseExtractor;
    private String baseRestUrl;

    public SpringRestDataProviderEngine(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.responseExtractor = new N2oResponseExtractor(objectMapper);
    }

    @Override
    public Class<? extends N2oRestDataProvider> getType() {
        return N2oRestDataProvider.class;
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
                str -> resolve(str, args, (a, b) -> a + invocation.getFiltersSeparator() + b),
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

    private static class N2oResponseExtractor implements ResponseExtractor<Object> {
        private ObjectMapper mapper;

        public N2oResponseExtractor(ObjectMapper objectMapper) {
            this.mapper = objectMapper;
        }

        @Override
        public Object extractData(ClientHttpResponse response) throws IOException {
            String result;
            try (InputStream body = response.getBody()) {
                result = IOUtils.toString(body, "UTF-8");
            }
            Object data = null;
            if (result != null && !result.isEmpty()) {
                result = result.trim();
                if (result.startsWith("["))
                    data = mapper.<List<DataSet>>readValue(result, mapper.getTypeFactory().constructCollectionType(List.class, DataSet.class));
                else if (result.startsWith("{"))
                    data = mapper.readValue(result, DataSet.class);
                else
                    data = mapper.readValue(result, Object.class);
            }
            return data;
        }
    }

    private Object executeQuery(String method, String query, Map<String, Object> args, String proxyHost,
                                Integer proxyPort) {
        query = getURL(proxyHost, proxyPort, query);
        HttpHeaders headers = initHeaders(args);
        Map<String, Object> body = new HashMap<>(args);

        switch (method) {
            case "GET":
                return exchange(query, HttpMethod.GET, headers);
            case "POST":
                return exchange(query, HttpMethod.POST, body, headers);
            case "PUT":
                return exchange(query, HttpMethod.PUT, body, headers);
            case "DELETE":
                return exchange(query, HttpMethod.DELETE, headers);
            default:
                throw new UnsupportedOperationException("Method " + method + " unsupported");
        }
    }

    private Object exchange(String query, HttpMethod method, HttpHeaders headers) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(headers), Object.class);
        return restTemplate.execute(query, method, requestCallback, responseExtractor);
    }

    private Object exchange(String query, HttpMethod method, Object body, HttpHeaders headers) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(body, headers), Object.class);
        return restTemplate.execute(query, method, requestCallback, responseExtractor);
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
        String paramKey = str.substring(str.indexOf('{') + 1, str.indexOf('}'));
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

}
