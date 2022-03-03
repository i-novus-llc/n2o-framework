package net.n2oapp.framework.boot.graphql;

import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;
import net.n2oapp.framework.engine.data.QueryUtil;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.n2oapp.framework.engine.data.QueryUtil.replaceListPlaceholder;

/**
 * GraphQL провайдер данных
 */
public class GraphqlDataProviderEngine implements MapInvocationEngine<N2oGraphqlDataProvider> {
    @Setter
    private String endpoint;
    @Setter
    private RestTemplate restTemplate;

    private Pattern pattern = Pattern.compile("\\$\\w+");

    @Override
    public Class<? extends N2oGraphqlDataProvider> getType() {
        return N2oGraphqlDataProvider.class;
    }

    @Override
    public Object invoke(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        return execute(prepareQuery(invocation.getQuery(), data), initEndpoint(invocation.getEndpoint()), data);
    }

    private String prepareQuery(String query, Map<String, Object> data) {
        if (query == null)
            throw new N2oGraphqlException("Запрос не найден");
        Map<String, Object> args = new HashMap<>(data);
        query = replaceListPlaceholder(query, "{{select}}", args.remove("select"), "", QueryUtil::reduceSpace);
        return query;
    }

    private Object execute(String query, String endpoint, Map<String, Object> data) {
        Map<String, Object> payload = initPayload(query, data);
        return restTemplate.postForObject(endpoint, payload, DataSet.class);
    }

    private Map<String, Object> initPayload(String query, Map<String, Object> data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query);
        payload.put("variables", initVariables(query, data));
        return payload;
    }

    private Object initVariables(String query, Map<String, Object> data) {
        Set<String> variables = extractVariables(query);
        DataSet result = new DataSet();
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
