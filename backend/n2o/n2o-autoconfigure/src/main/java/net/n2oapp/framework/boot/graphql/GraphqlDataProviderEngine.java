package net.n2oapp.framework.boot.graphql;

import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GraphQL провайдер данных
 */
@Setter
public class GraphqlDataProviderEngine implements MapInvocationEngine<N2oGraphqlDataProvider> {

    private String endpoint;
    private RestTemplate restTemplate;
    private Map<String, Object> payload = new HashMap<>();

    @Override
    public Class<? extends N2oGraphqlDataProvider> getType() {
        return N2oGraphqlDataProvider.class;
    }

    @Override
    public Object invoke(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        return execute(invocation.getQuery(), initEndpoint(invocation.getEndpoint()), data);
    }

    private Object execute(String query, String endpoint, Map<String, Object> data) {
        initPayload(query, data);
        return restTemplate.postForObject(endpoint, payload, DataSet.class);
    }

    private void initPayload(String query, Map<String, Object> data) {
        if (query == null)
            throw new N2oGraphqlException("Запрос не найден");
        payload.put("query", query);
        payload.put("variables", initVariables(query, data));
    }

    private Object initVariables(String query, Map<String, Object> data) {
        Set<String> variables = extractVariables(query);
        DataSet result = new DataSet();
        for (String variable : variables) {
            if (!data.containsKey(variable))
                throw new N2oGraphqlException("Переменная запроса не найдена");
            result.add(variable, data.get(variable));
        }
        return result;
    }

    private Set<String> extractVariables(String query) {
        Set<String> variables = new HashSet<>();
        Pattern pattern = Pattern.compile("\\$\\w+");
        Matcher matcher = pattern.matcher(query);
        while (matcher.find())
            variables.add(query.substring(matcher.start(), matcher.end()).replace("$", ""));
        return variables;
    }

    private String initEndpoint(String invocationEndpoint) {
        if (invocationEndpoint != null)
            return invocationEndpoint;
        return endpoint;
    }
}
