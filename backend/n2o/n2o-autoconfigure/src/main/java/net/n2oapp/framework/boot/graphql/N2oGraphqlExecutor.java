package net.n2oapp.framework.boot.graphql;

import net.n2oapp.criteria.dataset.DataSet;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class N2oGraphqlExecutor implements GraphqlExecutor {

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private JSONObject payload = new JSONObject();

    public N2oGraphqlExecutor(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
    }

    @Override
    public Object execute(String query, String endpoint, Map<String, Object> data) {
        initPayload(query, data);
        HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);
        return restTemplate.postForObject(endpoint, entity, DataSet.class);
    }

    private void initPayload(String query, Map<String, Object> data) {
        if (query == null)
            throw new N2oGraphqlException("Запрос не найден");
        setQuery(query);
    }

    private void setQuery(String query) {
        payload.put("query", query);
    }
}
