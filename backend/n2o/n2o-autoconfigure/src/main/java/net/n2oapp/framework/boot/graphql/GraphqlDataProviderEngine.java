package net.n2oapp.framework.boot.graphql;

import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;
import net.n2oapp.framework.engine.data.QueryUtil;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.n2oapp.framework.engine.data.QueryUtil.replaceListPlaceholder;
import static net.n2oapp.framework.engine.data.QueryUtil.replacePlaceholder;

/**
 * GraphQL провайдер данных
 */
public class GraphqlDataProviderEngine implements MapInvocationEngine<N2oGraphqlDataProvider> {
    @Setter
    private String endpoint;
    @Setter
    private RestTemplate restTemplate;

    private Pattern pattern = Pattern.compile("\\$\\w+");
    private Set<String> keyArgs = Set.of("select", "filters", "sorting", "join", "limit", "offset", "page");

    @Override
    public Class<? extends N2oGraphqlDataProvider> getType() {
        return N2oGraphqlDataProvider.class;
    }

    @Override
    public Object invoke(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        return execute(invocation, prepareQuery(invocation, data), data);
    }

    private String prepareQuery(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        if (invocation.getQuery() == null)
            throw new N2oGraphqlException("Запрос не найден");
        return resolvePlaceHolders(invocation, data);
    }

    private String resolvePlaceHolders(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        String query = invocation.getQuery();
        Map<String, Object> args = new HashMap<>(data);

        query = replaceListPlaceholder(query, "{{select}}", args.remove("select"), "", QueryUtil::reduceSpace);
        if (invocation.getPageMapping() == null)
            query = replacePlaceholder(query, "{{page}}", args.remove("page"), "1");
        if (invocation.getSizeMapping() == null)
            query = replacePlaceholder(query, "{{size}}", args.remove("limit"), "10");
        query = resolveFilters(query, args, invocation.getFilterSeparator());

        for (String key : data.keySet()) {
            if (!keyArgs.contains(key)) {
                Object value = args.get(key);
                query = replacePlaceholder(query, "{{" + key + "}}", value instanceof String ? "\"" + value + "\"" : value, "");
            }
        }
        return query;
    }

    private String resolveFilters(String query, Map<String, Object> args, String filterSeparator) {
        if (args.get("filters") == null)
            return query;
        if (((List<Object>) args.get("filters")).size() > 1 && filterSeparator == null)
            throw new N2oGraphqlException("Не задан атрибут filter-separator");

        return replaceListPlaceholder(query, "{{filters}}", args.remove("filters"),
                "", (a, b) -> QueryUtil.reduceSeparator(a, b, filterSeparator));
    }

    private Object execute(N2oGraphqlDataProvider invocation, String query, Map<String, Object> data) {
        Map<String, Object> payload = initPayload(invocation, query, data);
        String endpoint = initEndpoint(invocation.getEndpoint());
        return restTemplate.postForObject(endpoint, payload, DataSet.class);
    }

    private Map<String, Object> initPayload(N2oGraphqlDataProvider invocation, String query, Map<String, Object> data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query);
        payload.put("variables", initVariables(invocation, query, data));
        return payload;
    }

    private Object initVariables(N2oGraphqlDataProvider invocation, String query, Map<String, Object> data) {
        Set<String> variables = extractVariables(query);
        DataSet result = new DataSet();

        if (invocation.getPageMapping() != null)
            data.put(invocation.getPageMapping(), data.get("page"));
        if (invocation.getSizeMapping() != null)
            data.put(invocation.getSizeMapping(), data.get("limit"));

        for (String variable : variables) {
            if (!data.containsKey(variable))
                throw new N2oGraphqlException(String.format("Значение переменной '%s' не задано", variable));
            result.add(variable, data.get(variable));
        }
        return result;
    }

    private Set<String> extractVariables(String query) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = pattern.matcher(query);
        while (matcher.find())
            variables.add(query.substring(matcher.start() + 1, matcher.end()));
        return variables;
    }

    private String initEndpoint(String invocationEndpoint) {
        return invocationEndpoint != null ? invocationEndpoint : endpoint;
    }
}
