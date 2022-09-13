package net.n2oapp.framework.boot.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphQlDataProvider;
import net.n2oapp.framework.engine.data.QueryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.n2oapp.framework.boot.graphql.GraphQlUtil.escapeJson;
import static net.n2oapp.framework.boot.graphql.GraphQlUtil.toGraphQlString;
import static net.n2oapp.framework.engine.data.QueryUtil.replaceListPlaceholder;
import static net.n2oapp.framework.engine.data.QueryUtil.replacePlaceholder;

/**
 * GraphQL провайдер данных
 */
@Slf4j
public class GraphQlDataProviderEngine implements MapInvocationEngine<N2oGraphQlDataProvider> {

    private static final String RESPONSE_ERROR_KEY = "errors";
    private static final String RESPONSE_ERROR_MESSAGE_KEY = "message";
    private static final String RESPONSE_DATA_KEY = "data";
    private final Pattern variablePattern = Pattern.compile("\\$\\w+");
    private final Pattern placeholderKeyPattern = Pattern.compile("\\$\\$\\w+\\s*:");
    private final Pattern selectKeyPattern = Pattern.compile("\\$\\$\\w+\\W");
    private final Pattern placeholderStringEscapePattern = Pattern.compile("\\$\\$\\$\\w+");

    @Value("${n2o.engine.graphql.endpoint:}")
    private String endpoint;
    @Value("${n2o.engine.graphql.access-token:}")
    private String accessToken;
    @Value("${n2o.engine.graphql.filter-separator:}")
    private String defaultFilterSeparator;
    @Value("${n2o.engine.graphql.sorting-separator:}")
    private String defaultSortingSeparator;
    @Value("${n2o.engine.graphql.filter-prefix:}")
    private String defaultFilterPrefix;
    @Value("${n2o.engine.graphql.filter-suffix:}")
    private String defaultFilterSuffix;
    @Value("${n2o.engine.graphql.sorting-prefix:}")
    private String defaultSortingPrefix;
    @Value("${n2o.engine.graphql.sorting-suffix:}")
    private String defaultSortingSuffix;

    @Value("${n2o.engine.graphql.data-over-errors:false}")
    private boolean dataOverErrors;

    @Setter
    private RestTemplate restTemplate;
    private ObjectMapper mapper;

    public GraphQlDataProviderEngine(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @Override
    public Class<? extends N2oGraphQlDataProvider> getType() {
        return N2oGraphQlDataProvider.class;
    }

    @Override
    public DataSet invoke(N2oGraphQlDataProvider invocation, Map<String, Object> data) {
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
    private DataSet execute(N2oGraphQlDataProvider invocation, String query, Map<String, Object> data) {
        Map<String, Object> payload = initPayload(invocation, query, data);
        String endpoint = initEndpoint(invocation.getEndpoint());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        addAuthorization(invocation, headers);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        try {
            DataSet result = restTemplate.postForObject(endpoint, entity, DataSet.class);
            checkErrors(result, query);
            return result;
        } catch (RestClientResponseException e) {
            try {
                checkErrors(mapper.readValue(e.getResponseBodyAsString(), DataSet.class), query);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
            throw e;
        }
    }

    /**
     * Проверка наличия RESPONSE_ERROR_KEY в ответе сервера
     *
     * @param response Ответ GraphQl сревера
     * @param query    Строка GraphQl запроса
     */
    private void checkErrors(DataSet response, String query) {
        if (response.containsKey(RESPONSE_ERROR_KEY)) {
            Object data = response.get(RESPONSE_DATA_KEY);
            if (data != null && !((DataSet) data).isEmpty() && dataOverErrors)
                return;

            log.error("Execution error with GraphQL query: " + query);
            throw new N2oGraphQlException(((DataSet) response.getList(RESPONSE_ERROR_KEY).get(0)).getString(RESPONSE_ERROR_MESSAGE_KEY),
                    query, response);
        }
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
            throw new N2oException("Строка GraphQl запроса не задана");
        return resolvePlaceholders(invocation, data);
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
    private String resolvePlaceholders(N2oGraphQlDataProvider invocation, Map<String, Object> data) {
        String query = invocation.getQuery();
        Map<String, Object> args = new HashMap<>(data);

        resolveHierarchicalSelect(args);
        query = replaceListPlaceholder(query, "$$select", args.remove("select"), "", QueryUtil::reduceSpace);
        if (args.get("sorting") != null) {
            String prefix = Objects.requireNonNullElse(invocation.getSortingPrefix(), defaultSortingPrefix);
            String suffix = Objects.requireNonNullElse(invocation.getSortingSuffix(), defaultSortingSuffix);
            args.put("sorting", QueryUtil.insertPrefixSuffix((List<String>) args.get("sorting"), prefix, suffix));
            String sortingSeparator = Objects.requireNonNullElse(invocation.getSortingSeparator(), defaultSortingSeparator);
            query = replaceListPlaceholder(query, "$$sorting", args.remove("sorting"),
                    "", (a, b) -> QueryUtil.reduceSeparator(a, b, sortingSeparator));
        }
        if (invocation.getPageMapping() == null)
            query = replacePlaceholder(query, "$$page", args.remove("page"), "1");
        if (invocation.getSizeMapping() == null)
            query = replacePlaceholder(query, "$$size", args.remove("limit"), "10");
        query = replacePlaceholder(query, "$$offset", args.remove("offset"), "0");
        if (args.get("filters") != null) {
            String prefix = Objects.requireNonNullElse(invocation.getFilterPrefix(), defaultFilterPrefix);
            String suffix = Objects.requireNonNullElse(invocation.getFilterSuffix(), defaultFilterSuffix);
            args.put("filters", QueryUtil.insertPrefixSuffix((List<String>) args.get("filters"), prefix, suffix));
            String filterSeparator = Objects.requireNonNullElse(invocation.getFilterSeparator(), defaultFilterSeparator);
            query = replaceListPlaceholder(query, "$$filters", args.remove("filters"),
                    "", (a, b) -> QueryUtil.reduceSeparator(a, b, filterSeparator));
        }

        Set<String> placeholderKeys = extractPlaceholderKeys(query);
        Set<String> escapeStringPlaceholders = extractEscapeStringPlaceholder(query);
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String placeholder = "$$".concat(entry.getKey());
            String value;
            if (escapeStringPlaceholders.contains(entry.getKey())) {
                placeholder = "$".concat(placeholder);
                value = escapeJson(toGraphQlString(entry.getValue()));
            } else {
                value = placeholderKeys.contains(entry.getKey()) ?
                        (String) entry.getValue() :
                        toGraphQlString(entry.getValue());
            }
            query = replacePlaceholder(query, placeholder, value, "null");
        }

        log.debug("Execute GraphQL query: " + query);
        return query;
    }

    /**
     * Замена плейсхолдеров в "select"
     *
     * @param args Данные
     */
    private void resolveHierarchicalSelect(Map<String, Object> args) {
        @SuppressWarnings("unchecked")//Всегда приходит в виде списка из select-expression
        List<String> selectExpressions = (List<String>) args.get("select");
        if (selectExpressions == null)
            return;
        List<String> resolvedExpressions = new ArrayList<>();
        for (String selectExpression : selectExpressions) {
            while (selectKeyPattern.matcher(selectExpression).find()) {
                selectExpression = resolveSelectKey(selectExpression, args);
            }
            resolvedExpressions.add(selectExpression);
        }
        args.put("select", resolvedExpressions);
    }

    /**
     * Замена плейсхолдера в select-expression
     *
     * @param selectExpression Выражение
     * @param args             Данные
     * @return Разрезолвленное выражение или исходное при отсутствии в нем плейсхолдеров $$
     */
    private String resolveSelectKey(String selectExpression, Map<String, Object> args) {
        Set<String> selectKeys = extract(selectExpression, selectKeyPattern, (s, m) -> s.substring(m.start() + 2, m.end() - 1));
        Optional<String> selectKey = selectKeys.stream().findFirst();
        if (selectKey.isEmpty())
            return selectExpression;
        if (selectKeys.size() > 1)
            throw new N2oException("Find more than one select key in expression " + selectExpression);

        @SuppressWarnings("unchecked")//Всегда приходит в виде списка из select-expression
        List<String> value = (List<String>) args.remove(selectKey.get());
        if (value == null)
            throw new N2oException(String.format("Value for placeholder %s not found ", "$$" + selectKey.get()));
        return replacePlaceholder(selectExpression, "$$" + selectKey.get(), String.join(" ", value), "");
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
                throw new N2oException(String.format("Значение переменной '%s' не задано", variable));
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
        return extract(query, variablePattern, (s, m) -> s.substring(m.start() + 1, m.end()));
    }

    /**
     * Получение множества ключей-плейсхолдеров из входной строки GraphQl запроса
     *
     * @param query Строка GraphQl запроса
     * @return Множество ключей-плейсхолдеров
     */
    private Set<String> extractPlaceholderKeys(String query) {
        return extract(query, placeholderKeyPattern, (s, m) -> s.substring(m.start() + 2, m.end() - 1).trim());
    }

    /**
     * Получение множества экранированных строковых плейсхолдеров
     *
     * @param query Строка GraphQl запроса
     * @return Множество экранированных строковых плейсхолдеров
     */
    private Set<String> extractEscapeStringPlaceholder(String query) {
        return extract(query, placeholderStringEscapePattern, (s, m) -> s.substring(m.start() + 3, m.end()));
    }

    /**
     * Получение множества значений из входной строки GraphQl запроса
     *
     * @param query    Строка GraphQl запроса
     * @param pattern  Паттерн, по которому будут находиться значения
     * @param function Функция, описывающая действия с найденными значениями
     * @return Множество значений
     */
    private Set<String> extract(String query, Pattern pattern, BiFunction<String, Matcher, String> function) {
        Set<String> result = new HashSet<>();
        Matcher matcher = pattern.matcher(query);
        while (matcher.find())
            result.add(function.apply(query, matcher));
        return result;
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
