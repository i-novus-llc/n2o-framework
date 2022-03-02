package net.n2oapp.framework.test;

import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.boot.graphql.GraphqlDataProviderEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"n2o.engine.graphql.endpoint=test"}
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GraphQlDataProviderEngineTest {

    @Autowired
    private GraphqlDataProviderEngine provider;

    @LocalServerPort
    private int appPort;

    @Captor
    ArgumentCaptor<Map<String, Object>> payloadCaptor;

    @Mock
    private RestTemplate restTemplate;


    @BeforeEach
    public void before() {
        provider.setRestTemplate(restTemplate);
    }

    @Test
    public void testSelectPlaceholder() {
        String queryPath = "/n2o/data/test/graphql/select";
        String url = "http://localhost:" + appPort + queryPath;
        Mockito.when(restTemplate.postForObject(Mockito.any(), payloadCaptor.capture(), Mockito.any()));

        ResponseEntity<GetDataResponse> response = new RestTemplate().getForEntity(url, GetDataResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // TODO
        assertEquals(Map.of(), payloadCaptor.getValue());

//        GetDataResponse result = response.getBody();
    }
}
