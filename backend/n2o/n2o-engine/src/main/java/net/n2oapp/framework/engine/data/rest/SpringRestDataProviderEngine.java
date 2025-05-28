package net.n2oapp.framework.engine.data.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.data.exception.N2oQueryExecutionException;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.rest.RestLoggingHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;

import static java.util.Collections.emptyList;
import static net.n2oapp.framework.engine.data.QueryUtil.*;
import static org.springframework.http.HttpMethod.GET;

/**
 * Сервис вызова Spring RestTemplate
 */
public class SpringRestDataProviderEngine implements MapInvocationEngine<N2oRestDataProvider> {
    private static final Logger log = LoggerFactory.getLogger(SpringRestDataProviderEngine.class);
    private final List<RestLoggingHandler> loggingHandlers;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private ResponseExtractor<ResponseEntity<Object>> responseExtractor;
    @Setter
    private String baseRestUrl;

    @Setter
    @Value("${n2o.engine.rest.forward-headers:}")
    private String forwardHeaders;

    @Setter
    @Value("${n2o.engine.rest.forward-cookies:}")
    private String forwardCookies;

    public SpringRestDataProviderEngine(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this(restTemplate, objectMapper, emptyList());
    }

    public SpringRestDataProviderEngine(RestTemplate restTemplate, ObjectMapper objectMapper, List<RestLoggingHandler> loggingHandlers) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.responseExtractor = new N2oResponseExtractor(objectMapper);
        this.loggingHandlers = loggingHandlers;
    }

    private URI getURI(String host, Integer port, String url, Map<String, Object> args) {
        if (host != null && port != null)
            url = "http://" + host + ":" + port + url;
        return restTemplate.getUriTemplateHandler().expand(url, args);
    }

    @Override
    public Class<? extends N2oRestDataProvider> getType() {
        return N2oRestDataProvider.class;
    }

    @Override
    public Object invoke(N2oRestDataProvider invocation, Map<String, Object> data) {
        String query = invocation.getQuery();
        if (query == null)
            throw new N2oException("query mustn't be null");
        query = query.trim();
        final HttpMethod method = invocation.getMethod() != null ?
                HttpMethod.valueOf(invocation.getMethod().name()) :
                GET;
        Map<String, Object> args = new HashMap<>(data);
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
        return executeQuery(method, query, args, invocation);
    }

    /**
     * Инициализация заголовков запроса
     *
     * @return Заголовки
     */
    protected HttpHeaders initHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

    private Object executeQuery(HttpMethod method, String query, Map<String, Object> args, N2oRestDataProvider invocation) {
        URI finalQuery = getURI(invocation.getProxyHost(), invocation.getProxyPort(), query, args);
        HttpHeaders headers = initHeaders();
        copyForwardedHeaders(resolveForwardedHeaders(invocation), headers);
        copyForwardedCookies(resolveForwardedCookies(invocation), headers);

        log.debug("Execute REST query: {}", finalQuery);
        try {
            ResponseEntity<Object> result = switch (method.name()) {
                case "GET" -> exchange(finalQuery, method, headers);
                case "DELETE", "POST", "PUT", "PATCH" -> exchange(finalQuery, method, args, headers);
                default -> throw new UnsupportedOperationException("Method " + method.name() + " unsupported");
            };
            if (result == null)
                return null;
            loggingHandlers.forEach(handler -> handler.handle(result.getStatusCode().value(), method, finalQuery.toString(), headers));
            return result.getBody();
        } catch (RestClientResponseException e) {
            loggingHandlers.forEach(handler -> handler.handleError(e, method, finalQuery.toString(), headers));
            throw new N2oQueryExecutionException(e.getMessage().replaceAll("[{}]", ""), finalQuery.toString(), e);
        }
    }

    private ResponseEntity<Object> exchange(URI query, HttpMethod method, HttpHeaders headers) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(headers), Object.class);
        return restTemplate.execute(query, method, requestCallback, responseExtractor);
    }

    private ResponseEntity<Object> exchange(URI query, HttpMethod method, Object body, HttpHeaders headers) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(body, headers), Object.class);
        return restTemplate.execute(query, method, requestCallback, responseExtractor);
    }

    /**
     * Парсинг и выбор заголовков для пересылки
     *
     * @param invocation Провайдер данных
     */
    private Set<String> resolveForwardedHeaders(N2oRestDataProvider invocation) {
        String headers = invocation.getForwardedHeaders() != null ? invocation.getForwardedHeaders() : forwardHeaders;
        return parseHeadersString(headers);
    }

    /**
     * Парсинг и выбор cookie для пересылки
     *
     * @param invocation Провайдер данных
     */
    private Set<String> resolveForwardedCookies(N2oRestDataProvider invocation) {
        String cookies = invocation.getForwardedCookies() != null ? invocation.getForwardedCookies() : forwardCookies;
        return parseHeadersString(cookies);
    }

    private String resolve(String str, Map<String, Object> args, BinaryOperator<String> reducer) {
        if (!str.contains("{") || !str.contains("}")) return str;
        String paramKey = str.substring(str.indexOf('{') + 1, str.indexOf('}'));
        if (!(args.get(paramKey) instanceof List paramKeyList)) {
            if (args.get(paramKey) == null) return "";
            args.put(paramKey, resolveType(str, args.get(paramKey)));
            return str;
        }
        List<Object> params = paramKeyList;
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

    private static class N2oResponseExtractor implements ResponseExtractor<ResponseEntity<Object>> {
        private final ObjectMapper mapper;

        public N2oResponseExtractor(ObjectMapper objectMapper) {
            this.mapper = objectMapper;
        }

        @Override
        public ResponseEntity<Object> extractData(ClientHttpResponse response) throws IOException {
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
            return ResponseEntity.status(response.getStatusCode().value()).headers(response.getHeaders()).body(data);
        }
    }
}
