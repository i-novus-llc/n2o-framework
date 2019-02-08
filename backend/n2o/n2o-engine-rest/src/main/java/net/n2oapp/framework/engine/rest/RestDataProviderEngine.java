package net.n2oapp.framework.engine.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.util.RestClient;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.n2oapp.framework.engine.data.QueryUtil.normalizeQueryParams;
import static net.n2oapp.framework.engine.data.QueryUtil.replaceListPlaceholder;


/**
 * Сервис вызова rest клиента
 */
public class RestDataProviderEngine implements MapInvocationEngine<N2oRestDataProvider> {

    private RestClient restClient;
    private String baseRestUrl;
    private ObjectMapper objectMapper;

    public RestDataProviderEngine(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
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
        query = replaceListPlaceholder(query, "{filters}", args.remove("filters"), "",
                createResolver(args, invocation.getFiltersSeparator()), (a, b) -> a + invocation.getFiltersSeparator() + b);
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
    protected Map<String, String> initHeaders(Map<String, Object> args) {
        return new HashMap<>();
    }


    /**
     * Конкатенирует повторяющиеся параметры
     *
     * @param args      Аргументы запроса
     * @param separator Разделитель параметров
     * @param key       целевой аргумент
     */
    private void normalize(String key, Map<String, Object> args, String separator) {
        if (args.get(key) != null) {
            for (String param : (List<String>) args.get(key)) {
                if (!param.contains("{") || !param.contains("}")) continue;
                String paramKey = param.substring(param.indexOf("{") + 1, param.indexOf("}"));
                if (args.get(paramKey) != null && args.get(paramKey) instanceof List) {
                    int repetitionsCount = ((List) args.get(paramKey)).size() - 1;
                    List<String> params = ((List<String>) args.get(key))
                            .stream()
                            .map(parameter -> {
                                String result = parameter;
                                for (int i = 0; i < repetitionsCount; i++) {
                                    result += separator + parameter;
                                }
                                return result;
                            })
                            .collect(Collectors.toList());
                    args.put(key, params);
                }
            }
        }
    }

    private Object executeQuery(String method, String query, Map<String, Object> args, String proxyHost,
                                Integer proxyPort) {

        Map<String, String> headers = initHeaders(args);
        args = new HashMap<>(args);

        switch (method) {
            case "GET":
                return restClient.GET(query, Collections.emptyMap(), headers, proxyHost, proxyPort);
            case "POST":
                return restClient.POST(query, args, headers, proxyHost, proxyPort);
            case "PUT":
                return restClient.PUT(query, args, headers, proxyHost, proxyPort);
            case "DELETE":
                return restClient.DELETE(query, Collections.emptyMap(), headers, proxyHost, proxyPort);
            case "HEAD":
                return restClient.HEAD(query, Collections.emptyMap(), headers, proxyHost, proxyPort);
            default:
                throw new UnsupportedOperationException("Method " + method + " unsupported");
        }
    }

    private String resolvePathPlaceholders(String query, Map<String, Object> args) {
        for (String key : new HashSet<>(args.keySet())) {
            String p = "{" + key + "}";
            if (query.contains(p)) {
                String value;
                try {
                    value = args.get(key) == null ? "" : RestUtil.encode(
                            objectMapper.writeValueAsString(args.get(key)).replace("\"", "")
                    );
                } catch (JsonProcessingException e) {
                    throw new N2oException(e);
                }
                query = query.replace("{" + key + "}", value);
                args.remove(key);
            }
        }
        return query;
    }

    private Function<String, String> createResolver(Map<String, Object> args, String separator) {
        return str -> {
            if (!str.contains("{") || !str.contains("}")) return str;
            String paramKey = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
            if (!(args.get(paramKey) instanceof List)) return str;
            List<String> paramValues = ((List) args.get(paramKey));
            int size = paramValues.size();
            int i = 0;
            String result = str.replaceFirst("\\{" + paramKey.replace("*.", "\\*\\.") + "}", paramValues.get(i++));
            for (int j = 0; j < size - 1; j++) {
                result += separator + str.replaceFirst("\\{" + paramKey.replace("*.", "\\*\\.") + "}", paramValues.get(i++));
            }
            return result;
        };
    }

    @Override
    public Class<? extends N2oRestDataProvider> getType() {
        return N2oRestDataProvider.class;
    }

}
