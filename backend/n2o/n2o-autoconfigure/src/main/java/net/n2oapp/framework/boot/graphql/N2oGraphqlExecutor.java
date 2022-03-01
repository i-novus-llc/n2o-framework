package net.n2oapp.framework.boot.graphql;

import net.n2oapp.criteria.dataset.DataSet;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация отправки GraphQL запросов на удаленный сервер с использованием RestTemplate
 */
public class N2oGraphqlExecutor implements GraphqlExecutor {

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private Map<String, Object> payload = new HashMap<>();

    public N2oGraphqlExecutor(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
    }

    @Override
    public Object execute(String query, String endpoint, Map<String, Object> data) {
        initPayload(query, data);
        return restTemplate.postForObject(endpoint, payload, DataSet.class);
    }

    private void initPayload(String query, Map<String, Object> data) {
        if (query == null)
            throw new N2oGraphqlException("Запрос не найден");
        payload.put("query", query);
    }
}
