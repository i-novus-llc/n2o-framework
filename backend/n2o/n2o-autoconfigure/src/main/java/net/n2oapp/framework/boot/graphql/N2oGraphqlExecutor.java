package net.n2oapp.framework.boot.graphql;

import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class N2oGraphqlExecutor implements GraphqlExecutor {

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private GraphqlPayload payload;

    public N2oGraphqlExecutor(RestTemplate restTemplate, HttpHeaders headers, GraphqlPayload payload) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.payload = payload;
    }

    @Override
    public Object execute(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        String endpoint = initEndpoint(invocation);
        initPayload(invocation, data);
        HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);
        return restTemplate.postForObject(endpoint, entity, String.class);
    }

    private void initPayload(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        if (invocation.getQuery() == null)
            throw new N2oGraphqlException("Запрос не найден");
        payload.setQuery(invocation.getQuery());
    }

    private String initEndpoint(N2oGraphqlDataProvider invocation) {
        if (invocation.getEndpoint() == null)
            throw new N2oGraphqlException("Не задан endpoint");
        return invocation.getEndpoint();
    }

}
