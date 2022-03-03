package net.n2oapp.framework.test;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
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

import java.util.*;

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
    private RestTemplate restTemplateMock;

    private RestTemplate restTemplate = new RestTemplate();


    @BeforeEach
    public void before() {
        provider.setRestTemplate(restTemplateMock);
        provider.setEndpoint("http://graphql");
    }

    /**
     * Проверка отправки запроса с переменными
     */
    // TODO - nesting field filtering
    @Test
    public void testQueryWithVariables() {
        String queryPath = "/n2o/data/test/graphql/query/variables?personName=name2&age=20";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("id", 2,
                        "name", "name2",
                        "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query Persons($name: String, $age: Int) { persons(name: $name, age: $age) {id name age}";
        Mockito.when(restTemplateMock.postForObject(anyString(), anyMap(), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        Mockito.verify(restTemplateMock).postForObject(anyString(), payloadCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = payloadCaptor.getValue();

        // graphql payload
        assertEquals("name2", ((DataSet) payloadValue.get("variables")).get("name"));
        assertEquals(20, ((DataSet) payloadValue.get("variables")).get("age"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // response
        GetDataResponse result = response.getBody();
        assertEquals(1, result.getList().size());
        assertEquals(2, result.getList().get(0).get("id"));
        assertEquals("name2", result.getList().get(0).get("personName"));
        assertEquals(20, result.getList().get(0).get("age"));
    }

    /**
     * Проверка отправки мутации с переменными
     */
    @Test
    public void testMutationWithVariables() {
        String queryPath = "/n2o/data/test/graphql/mutationVariables";
        String url = "http://localhost:" + appPort + queryPath;
        Request request = new Request("newName", 99, List.of(new Address("address1")));

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("createPerson",
                new HashMap<>(Map.of("id", 1,
                        "name", request.getPersonName(),
                        "age", request.getAge(),
                        "addresses", request.getAddresses())));
        data.put("data", persons);

        String expectedQuery = "mutation CreatePerson($name: String!, $age: Int!, $addresses: [Address!]) " +
                "{ createPerson(name: $name, age: $age, addresses: $addresses) {id name age address: {street}}";
        Mockito.when(restTemplateMock.postForObject(anyString(), anyMap(), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        SetDataResponse response = restTemplate.postForObject(url, request, SetDataResponse.class);
        Mockito.verify(restTemplateMock).postForObject(anyString(), payloadCaptor.capture(), eq(DataSet.class));
        assertEquals("success", response.getMeta().getAlert().getMessages().get(0).getColor());
        Map<String, Object> payloadValue = payloadCaptor.getValue();

        // graphql payload
        assertEquals(request.getPersonName(), ((DataSet) payloadValue.get("variables")).get("name"));
        assertEquals(request.getAge(), ((DataSet) payloadValue.get("variables")).get("age"));
        assertEquals(request.getAddresses().get(0).getStreet(),
                ((DataSet) ((DataList) ((DataSet) payloadValue.get("variables")).get("addresses")).get(0)).get("street"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // response
        Map<String, Object> result = response.getData();
        assertEquals(request.getId(), result.get("id"));
        assertEquals(request.getPersonName(), result.get("personName"));
        assertEquals(request.getAge(), result.get("age"));
        assertEquals(request.getAddresses().get(0).getStreet(), ((Map) ((List) result.get("addresses")).get(0)).get("street"));
    }

    /**
     * Проверка работы плейсхолдера {{select}}
     */
    @Test
    public void testSelectPlaceholder() {
        String queryPath = "/n2o/data/test/graphql/select";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Arrays.asList(
                Map.of("name", "test1", "age", 10),
                Map.of("name", "test2", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons() { name age }";
        Mockito.when(restTemplateMock.postForObject(anyString(), anyMap(), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        Mockito.verify(restTemplateMock).postForObject(anyString(), payloadCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = payloadCaptor.getValue();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));
    }


    @Getter
    @Setter
    public static class Request {
        private Integer id;
        private String personName;
        private Integer age;
        private List<Address> addresses;

        public Request(String name, Integer age, List<Address> addresses) {
            this.personName = name;
            this.age = age;
            this.addresses = addresses;
        }
    }

    @Getter
    @Setter
    public static class Address {
        private String street;

        public Address(String street) {
            this.street = street;
        }
    }
}
