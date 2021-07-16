package net.n2oapp.framework.engine.data.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
        query = query.trim();
        final HttpMethod method = invocation.getMethod() == null ? HttpMethod.GET : HttpMethod.resolve(invocation.getMethod().name());
        Map<String, Object> args = new HashMap<>();
        data.forEach(args::put);
        if (!query.contains("http")) {
            if (!query.startsWith("/")) {
                query = "/" + query;
            }
            if (query.contains("//")) {
                query = query.replace("//", "/");
            }
            query = baseRestUrl + query;
        }

        String filtersSeparator = invocation.getFiltersSeparator() != null ? invocation.getFiltersSeparator() : "&";
        String sortingSeparator = invocation.getSortingSeparator() != null ? invocation.getSortingSeparator() : "&";
        String joinSeparator = invocation.getJoinSeparator() != null ? invocation.getJoinSeparator() : "&";
        query = replaceListPlaceholder(query, "{select}", args.remove("select"), "", (a, b) -> a + invocation.getSelectSeparator() + b);
        query = replaceListPlaceholder(query,
                "{filters}",
                args.remove("filters"), "",
                str -> resolve(str, args, (a, b) -> a + filtersSeparator + b),
                (a, b) -> a + filtersSeparator + b);
        query = replaceListPlaceholder(query, "{sorting}", args.remove("sorting"), "", (a, b) -> a + sortingSeparator + b);
        query = replaceListPlaceholder(query, "{join}", args.remove("join"), "", (a, b) -> a + joinSeparator + b);
        query = normalizeQueryParams(query);
        return executeQuery(method, query, args, invocation.getProxyHost(), invocation.getProxyPort());
    }

    /**
     * Инициализация заголовков запроса
     *
     * @param args Аргументы запроса
     * @return Заголовки
     */
    protected HttpHeaders initHeaders(Map<String, Object> args) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
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
                result = IOUtils.toString(body, StandardCharsets.UTF_8);
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

    private Object executeQuery(HttpMethod method, String query, Map<String, Object> args, String proxyHost,
                                Integer proxyPort) {
        query = getURL(proxyHost, proxyPort, query);
        HttpHeaders headers = initHeaders(args);
        Map<String, Object> body = new HashMap<>(args);

        switch (method) {
            case GET:
                return exchange(query, method, headers, args);
            case DELETE:
            case POST:
            case PUT:
            case PATCH:
                return exchange(query, method, body, headers, args);
            default:
                throw new UnsupportedOperationException("Method " + method.name() + " unsupported");
        }
    }

    private Object exchange(String query, HttpMethod method, HttpHeaders headers, Map<String, Object> args) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(headers), Object.class);
        return restTemplate.execute(query, method, requestCallback, responseExtractor, args);
    }

    private Object exchange(String query, HttpMethod method, Object body, HttpHeaders headers, Map<String, Object> args) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(body, headers), Object.class);
        return restTemplate.execute(query, method, requestCallback, responseExtractor, args);
    }

    private String resolve(String str, Map<String, Object> args, BinaryOperator<String> reducer) {
        if (!str.contains("{") || !str.contains("}")) return str;
        String paramKey = str.substring(str.indexOf('{') + 1, str.indexOf('}'));
        if (!(args.get(paramKey) instanceof List)) {
            if (args.get(paramKey) == null) return "";
            args.put(paramKey, resolveType(str, args.get(paramKey)));
            return str;
        }
        List<Object> params = ((List<Object>) args.get(paramKey));
        AtomicInteger i = new AtomicInteger();
        Optional<String> result = params
                .stream()
                .map(item -> {
                            if (item == null) return "";
                            String newParamKey = paramKey + i.incrementAndGet();
                            args.put(newParamKey, resolveType(str, item));
                            return str.replace(Placeholders.ref(paramKey), Placeholders.ref(newParamKey));
                        }
                )
                .reduce(reducer);
        return result.orElse("");
    }

    private String resolveType(String target, Object value) {
        if (value == null)
            return target;
        String result;
        if (value instanceof String || value instanceof Boolean)
            result = value.toString();
        else {
            try {
                result = objectMapper.writeValueAsString(value).replace("\"", "");
            } catch (JsonProcessingException e) {
                throw new N2oException(e);
            }
        }

        return result;
    }

    private static String getURL(String host, Integer port, String url) {
        if (host == null || port == null)
            return url;
        else
            return "http://" + host + ":" + port + url;
    }

}
