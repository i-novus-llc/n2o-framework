package net.n2oapp.framework.test;

import net.n2oapp.criteria.dataset.DataSet;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GraphQlDataProviderEngineTest {

    @LocalServerPort
    private int appPort;

    @Captor
    ArgumentCaptor<Map<String, Object>> payloadCaptor;

    @Autowired
    private GraphqlDataProviderEngine provider;

    @Mock
    private RestTemplate restTemplate;


    @BeforeEach
    public void before() {
        provider.setRestTemplate(restTemplate);
        provider.setEndpoint("http://graphql");
    }

    /**
     * Проверка работы плейсхолдера {{select}}
     */
    @Test
    public void testSelectPlaceholder() {
        String queryPath = "/n2o/data/test/graphql/select";
        String url = "http://localhost:" + appPort + queryPath;

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Arrays.asList(
                Map.of("name", "test1", "age", 10),
                Map.of("name", "test2", "age", 20)));
        data.put("data", persons);

        Mockito.when(restTemplate.postForObject(anyString(), anyMap(), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = new RestTemplate().getForEntity(url, GetDataResponse.class);
        Mockito.verify(restTemplate).postForObject(anyString(), payloadCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = payloadCaptor.getValue();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals("query persons() { name age }", payloadValue.get("query"));

        // response
        GetDataResponse result = response.getBody();
        assertEquals(2, result.getList().size());
        assertEquals("test1", result.getList().get(0).get("name"));
        assertEquals(10, result.getList().get(0).get("age"));
        assertEquals("test2", result.getList().get(1).get("name"));
        assertEquals(20, result.getList().get(1).get("age"));
    }
}
