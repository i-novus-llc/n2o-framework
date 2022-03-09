package net.n2oapp.framework.boot.graphql;

import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphQlDataProvider;
import net.n2oapp.framework.engine.data.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.PropertyResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.n2oapp.framework.boot.graphql.GraphQlUtil.toGraphQlString;
import static net.n2oapp.framework.engine.data.QueryUtil.replaceListPlaceholder;
import static net.n2oapp.framework.engine.data.QueryUtil.replacePlaceholder;

/**
 * GraphQL провайдер данных
 */
public class GraphQlDataProviderEngine implements MapInvocationEngine<N2oGraphQlDataProvider> {

    @Value("${n2o.engine.graphql.endpoint:}")
    private String endpoint;
    @Value("${n2o.engine.graphql.access-token:}")
    private String accessToken;
    @Autowired
    private PropertyResolver propertyResolver;

    @Setter
    private RestTemplate restTemplate;

    private Pattern pattern = Pattern.compile("\\$\\w+");
    private Set<String> keyArgs = Set.of("select", "filters", "sorting", "join", "limit", "offset", "page");


    @Override
    public Class<? extends N2oGraphQlDataProvider> getType() {
        return N2oGraphQlDataProvider.class;
    }

    @Override
    public Object invoke(N2oGraphQlDataProvider invocation, Map<String, Object> data) {
        return execute(invocation, prepareQuery(invocation, data), data);
    }

    /**
     * Формирование и отправка GraphQl запроса
     *
     * @param invocation Провайдер данных
     * @param query      Строка GraphQl запроса
     * @param data       Входные данные
     * @return Исходящие данные
     */
    private Object execute(N2oGraphQlDataProvider invocation, String query, Map<String, Object> data) {
        Map<String, Object> payload = initPayload(invocation, query, data);
        String endpoint = initEndpoint(invocation.getEndpoint());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        addAuthorization(invocation, headers);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        return restTemplate.postForObject(endpoint, entity, DataSet.class);
    }

    /**
     * Добавление авторизации в хэдер запроса
     *
     * @param invocation Провайдер данных
     * @param headers    Хэдер запроса
     */
    private void addAuthorization(N2oGraphQlDataProvider invocation, HttpHeaders headers) {
        String token = invocation.getAccessToken() != null ?
            invocation.getAccessToken() : accessToken;
        headers.set("Authorization", "Bearer " + token);
    }

    /**
     * Формирование GraphQl запроса
     *
     * @param invocation Провайдер данных
     * @param data       Входные данные
     * @return GraphQl запрос
     */
    private String prepareQuery(N2oGraphQlDataProvider invocation, Map<String, Object> data) {
        if (invocation.getQuery() == null)
            throw new N2oGraphQlException("Строка GraphQl запроса не задана");
        return resolvePlaceHolders(invocation, data);
    }

    /**
     * Инициализация payload для отправки
     *
     * @param invocation Провайдер данных
     * @param query      Строка GraphQl запроса
     * @param data       Входные данные
     * @return Payload
     */
    private Map<String, Object> initPayload(N2oGraphQlDataProvider invocation, String query, Map<String, Object> data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query);
        payload.put("variables", initVariables(invocation, query, data));
        return payload;
    }

    /**
     * Замена плейсхолдеров во входной строке GraphQl запроса
     *
     * @param invocation Провайдер данных
     * @param data       Входные данные
     * @return Строка GraphQl запроса с плейсхолдерами, замененными данными
     */
    private String resolvePlaceHolders(N2oGraphQlDataProvider invocation, Map<String, Object> data) {
        String query = invocation.getQuery();
        Map<String, Object> args = new HashMap<>(data);

        query = replaceListPlaceholder(query, "{{select}}", args.remove("select"), "", QueryUtil::reduceSpace);
        query = replaceListPlaceholder(query, "{{sorting}}", args.remove("sorting"), "", (a, b) -> QueryUtil.reduceSeparator(a, b, ", "));
        if (invocation.getPageMapping() == null)
            query = replacePlaceholder(query, "{{page}}", args.remove("page"), "1");
        if (invocation.getSizeMapping() == null)
            query = replacePlaceholder(query, "{{size}}", args.remove("limit"), "10");
        if (args.get("filters") != null && (invocation.getFilterSeparator() != null || ((List) args.get("filters")).size() == 1))
            query = replaceListPlaceholder(query, "{{filters}}", args.remove("filters"),
                    "", (a, b) -> QueryUtil.reduceSeparator(a, b, invocation.getFilterSeparator()));

        for (String key : data.keySet()) {
            if (!keyArgs.contains(key)) {
                String value = toGraphQlString(args.get(key));
                query = replacePlaceholder(query, "{{" + key + "}}", value, "");
            }
        }
        return query;
    }

    /**
     * Инициализация множества переменных GraphQl запроса значениями
     *
     * @param invocation Провайдер данных
     * @param query      Строка GraphQl запроса
     * @param data       Входные данные
     * @return Объект со значениями переменных GraphQl запроса
     */
    private Object initVariables(N2oGraphQlDataProvider invocation, String query, Map<String, Object> data) {
        Set<String> variables = extractVariables(query);
        DataSet result = new DataSet();

        if (invocation.getPageMapping() != null)
            data.put(invocation.getPageMapping(), data.get("page"));
        if (invocation.getSizeMapping() != null)
            data.put(invocation.getSizeMapping(), data.get("limit"));

        for (String variable : variables) {
            if (!data.containsKey(variable))
                throw new N2oGraphQlException(String.format("Значение переменной '%s' не задано", variable));
            result.add(variable, data.get(variable));
        }
        return result;
    }

    /**
     * Получение множества переменных из входной строки GraphQl запроса
     *
     * @param query Строка GraphQl запроса
     * @return Множество переменных
     */
    private Set<String> extractVariables(String query) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = pattern.matcher(query);
        while (matcher.find())
            variables.add(query.substring(matcher.start() + 1, matcher.end()));
        return variables;
    }

    /**
     * Инициализация эндпоинта
     *
     * @param invocationEndpoint Эндпоинт, заданный в моделе провайдера
     * @return Эндпоинт
     */
    private String initEndpoint(String invocationEndpoint) {
        return invocationEndpoint != null ? invocationEndpoint : endpoint;
    }
}
