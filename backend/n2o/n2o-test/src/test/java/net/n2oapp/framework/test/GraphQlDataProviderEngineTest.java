package net.n2oapp.framework.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.boot.graphql.GraphQlDataProviderEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"n2o.engine.graphql.endpoint=http://graphql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GraphQlDataProviderEngineTest {

    @LocalServerPort
    private int appPort;

    @Captor
    ArgumentCaptor<HttpEntity> httpEntityCaptor;

    @Autowired
    private GraphQlDataProviderEngine provider;

    @Mock
    private RestTemplate restTemplateMock;

    private RestTemplate restTemplate = new RestTemplate();


    @BeforeEach
    public void before() {
        provider.setRestTemplate(restTemplateMock);
    }

    /**
     * Проверка отправки запроса с переменными
     */
    @Test
    public void testQueryWithVariables() {
        String queryPath = "/n2o/data/test/graphql/query/variables?personName=test&age=20&address.name=address1&address.name=address2";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                new HashMap<>(Map.of("id", 2,
                        "name", "test",
                        "age", 20,
                        "addresses", List.of(Map.of("street", "address1"), Map.of("street", "address2"))))));
        data.put("data", persons);

        String expectedQuery = "query Persons($name: String, $age: Int, $addresses: [Address!]) " +
                "{ persons(name: $name, age: $age, addresses: $addresses) {id name age} }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals("test", ((DataSet) payloadValue.get("variables")).get("name"));
        assertEquals(20, ((DataSet) payloadValue.get("variables")).get("age"));
        DataList addresses = (DataList) ((DataSet) payloadValue.get("variables")).get("addresses");
        assertEquals(2, addresses.size());
        assertEquals("address1", addresses.get(0));
        assertEquals("address2", addresses.get(1));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // response
        GetDataResponse result = response.getBody();
        assertEquals(1, result.getList().size());
        assertEquals(2, result.getList().get(0).get("id"));
        assertEquals("test", result.getList().get(0).get("personName"));
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
                        "addresses", List.of(Map.of("street", "address1")))));
        data.put("data", persons);

        String expectedQuery = "mutation CreatePerson($name: String!, $age: Int!, $addresses: [Address!]) " +
                "{ createPerson(name: $name, age: $age, addresses: $addresses) {id name age address: {street}} }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        SetDataResponse response = restTemplate.postForObject(url, request, SetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals("success", response.getMeta().getAlert().getMessages().get(0).getColor());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

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
        persons.put("persons", Collections.singletonList(
                Map.of("name", "test", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons() { name age }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));
    }

    /**
     * Проверка работы плейсхолдера {{filters}}
     */
    @Test
    public void testFiltersPlaceholder() {
        // TWO FILTERS
        String queryPath = "/n2o/data/test/graphql/filters?personName=test&age=20";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("name", "test", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons(" +
                "filter: { { name: {eq: \"test\" } } AND { age: {ge: 20 } } }) " +
                "{id name age}";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // ONE FILTER (without separator)
        queryPath = "/n2o/data/test/graphql/filters?personName=test";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query persons(" +
                "filter: { { name: {eq: \"test\" } } }) " +
                "{id name age}";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));
    }

    /**
     * Проверка работы плейсхолдеров в выборке
     */
    @Test
    public void testQueryWithPlaceholders() {
        String queryPath = "/n2o/data/test/graphql/filters?personName=test&age=20&address.name=address1&address.name=address2";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                new HashMap<>(Map.of("id", 2,
                        "name", "test",
                        "age", 20,
                        "addresses", List.of(Map.of("street", "address1"), Map.of("street", "address2"))))));
        data.put("data", persons);

        String expectedQuery = "query persons(name: \"test\", age: 20, addresses: [\"address1\", \"address2\"]) {id name age}";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // response
        GetDataResponse result = response.getBody();
        assertEquals(1, result.getList().size());
        assertEquals(2, result.getList().get(0).get("id"));
        assertEquals("test", result.getList().get(0).get("personName"));
        assertEquals(20, result.getList().get(0).get("age"));
        assertEquals("address1", ((Map) ((List) result.getList().get(0).get("addresses")).get(0)).get("street"));
        assertEquals("address2", ((Map) ((List) result.getList().get(0).get("addresses")).get(1)).get("street"));
    }

    /**
     * Проверка работы плейсхолдеров в мутации
     */
    @Test
    public void testMutationWithPlaceholders() {
        String queryPath = "/n2o/data/test/graphql/mutationPlaceholders";
        String url = "http://localhost:" + appPort + queryPath;
        Request request = new Request("newName", 99, List.of(new Address("address1")));

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("createPerson",
                new HashMap<>(Map.of("id", 1,
                        "name", request.getPersonName(),
                        "age", request.getAge(),
                        "addresses", List.of(Map.of("street", "address1")))));
        data.put("data", persons);

        String expectedQuery = "mutation { createPerson(name: \"newName\", age: 99, " +
                "addresses: [{street: \"address1\"}]) {id name age address: {street}} }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        SetDataResponse response = restTemplate.postForObject(url, request, SetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals("success", response.getMeta().getAlert().getMessages().get(0).getColor());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // response
        Map<String, Object> result = response.getData();
        assertEquals(request.getId(), result.get("id"));
        assertEquals(request.getPersonName(), result.get("personName"));
        assertEquals(request.getAge(), result.get("age"));
        assertEquals(request.getAddresses().get(0).getStreet(), ((Map) ((List) result.get("addresses")).get(0)).get("street"));
    }

    @Test
    public void testPagination() {
        // PLACEHOLDERS
        String queryPath = "/n2o/data/test/graphql/pagination?personName=test&page=2&size=5";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("name", "test", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons(page: 2, size: 5, offset: 5) {id name age}";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // VARIABLES
        queryPath = "/n2o/data/test/graphql/pagination?age=20&page=3&size=15";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query Persons($pageNum: Int!, $sizeNum: Int!) " +
                "{ persons(page: $pageNum, size: $sizeNum) {id name age} }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(3, ((DataSet) payloadValue.get("variables")).get("pageNum"));
        assertEquals(15, ((DataSet) payloadValue.get("variables")).get("sizeNum"));
        assertEquals(expectedQuery, payloadValue.get("query"));
    }

    /**
     * Проверка работы плейсхолдера {{sorting}}
     */
    @Test
    public void testSortingPlaceholder() {
        // MULTIPLE SORTS
        String queryPath = "/n2o/data/test/graphql/sorting?sorting.name=ASC&sorting.age=DESC";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("name", "test", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons(sort: { {name: \"asc\"}, {age: \"desc\"} }) { name age }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // ONE SORT
        queryPath = "/n2o/data/test/graphql/sorting?sorting.name=ASC";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query persons(sort: { {name: \"asc\"} }) { name age }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(Collections.emptyMap(), payloadValue.get("variables"));
        assertEquals(expectedQuery, payloadValue.get("query"));

        // ONE SORT (another option)
        queryPath = "/n2o/data/test/graphql/sorting?id=5&sorting.id=ASC";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query persons( sort: { asc : \"id\" } ) { name age }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock, atLeastOnce()).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

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
