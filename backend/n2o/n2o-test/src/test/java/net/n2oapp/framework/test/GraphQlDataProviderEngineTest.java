package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
        String queryPath = "/n2o/data/test/graphql/query/variables?personName=t\"es\"t&age=20&address.name=address1&address.name=address2";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                new HashMap<>(Map.of("id", 2,
                        "name", "t\"es\"t",
                        "age", 20,
                        "addresses", List.of(Map.of("street", "address1"), Map.of("street", "address2"))))));
        data.put("data", persons);

        String expectedQuery = "query Persons($name: String, $age: Int, $addresses: [Address!])" +
                " { persons(name: $name, age: $age, addresses: $addresses) {id name age} }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals("t\"es\"t", ((DataSet) payloadValue.get("variables")).get("name"));
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
        assertEquals("t\"es\"t", result.getList().get(0).get("personName"));
        assertEquals(20, result.getList().get(0).get("age"));
    }

    /**
     * Проверка проброса заголовков клиента
     */
    @Test
    public void testHeadersAndCookiesForwarding() {
        String headersForwardingQueryPath = "/n2o/data/test/graphql/query/headersForwarding";
        String headersForwardingFromPropertiesQueryPath = "/n2o/data/test/graphql/select";
        String url = "http://localhost:" + appPort + headersForwardingQueryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                new HashMap<>(Map.of("id", 2,
                        "name", "test"))));
        data.put("data", persons);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("testForwardedHeader", "ForwardedHeaderValue");
        headers.add("testNotForwardHeader", "NotForwardHeaderValue");
        headers.add("Cookie", "c1=c1Value;c2=c2Value;c3=c3Value;");


        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), GetDataResponse.class);

        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        HttpHeaders httpHeaders = httpEntityCaptor.getValue().getHeaders();
        assertEquals("ForwardedHeaderValue", httpHeaders.get("testForwardedHeader").get(0));
        assertEquals("c3=c3Value;c1=c1Value", httpHeaders.get("cookie").get(0));
        assertEquals(Boolean.FALSE, httpHeaders.containsKey("testNotForwardHeader"));

        headers.add("testHeaderFromProperty1", "testHeaderFromProperty1Value");
        headers.add("testHeaderFromProperty2", "testHeaderFromProperty2Value");
        headers.add("Cookie", "cfp1=cfp1Value;cfp2=cfp2Value;cfp3=cfp3Value;");

        url = "http://localhost:" + appPort + headersForwardingFromPropertiesQueryPath;

        restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), GetDataResponse.class);

        verify(restTemplateMock, times(2)).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        httpHeaders = httpEntityCaptor.getValue().getHeaders();
        assertEquals("testHeaderFromProperty1Value", httpHeaders.get("testHeaderFromProperty1").get(0));
        assertEquals("testHeaderFromProperty2Value", httpHeaders.get("testHeaderFromProperty2").get(0));
        assertEquals("cfp2=cfp2Value", httpHeaders.get("cookie").get(0));
        assertEquals(Boolean.FALSE, httpHeaders.containsKey("testForwardedHeader"));
        assertEquals(Boolean.FALSE, httpHeaders.containsKey("testNotForwardHeader"));
    }

    /**
     * Проверка отправки мутации с переменными
     */
    @Test
    public void testMutationWithVariables() {
        String queryPath = "/n2o/data/test/graphql/mutationVariables";
        String url = "http://localhost:" + appPort + queryPath;
        Request request = new Request("new\\\"Name\\\"", 99, List.of(new Address("address1")));

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
        assertEquals("success", response.getMeta().getAlert().getMessages().get(0).getSeverity());
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
     * Тестирование кастомной обработки ошибок
     */
    @Test
    public void testErrorHandler() throws IOException {
        DataList errors = new DataList();
        Map<String, Object> data = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        provider.setRestTemplate(restTemplateMock);

        String queryPath = "/n2o/data/test/graphql/mutationVariables";
        Request request = new Request("newName", 99, List.of(new Address("address1")));
        String url = "http://localhost:" + appPort + queryPath;

        //graphql error payload
        DataSet ds = new DataSet();
        ds.put("message", "Invalid field type");
        ds.put("line", 3);
        ds.put("column", 1);

        errors.add(ds);
        data.put("errors", errors);

        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        //test error message
        try {
            restTemplate.postForObject(url, request, SetDataResponse.class);
        } catch (HttpServerErrorException e) {
            SetDataResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), SetDataResponse.class);
            assertEquals("Message: Invalid field type, line: 3, column: 1.",
                    resp.getMeta().getAlert().getMessages().get(0).getText());
        }
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
        String queryPath = "/n2o/data/test/graphql/filters?personName=t\"es\"t&age=20&salary=123.55";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("name", "t\"es\"t", "age", 20, "salary", 123.55)));
        data.put("data", persons);

        String expectedQuery = "query persons(" +
                "filter: { [{ name: {like: \"\\\"t\\\\\\\"es\\\\\\\"t\\\"\" } }] AND [{ age: {ge: 20 } }] AND [{ salary: {ge: \"123.55\" } }] }) " +
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
        queryPath = "/n2o/data/test/graphql/filters?personName=t\"es\"t";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query persons(" +
                "filter: { [{ name: {like: \"\\\"t\\\\\\\"es\\\\\\\"t\\\"\" } }] }) " +
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
        String queryPath = "/n2o/data/test/graphql/filters?personName=t\"es\"t&age=20&address.name=address1&address.name=address2";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                new HashMap<>(Map.of("id", 2,
                        "name", "t\"es\"t",
                        "age", 20,
                        "addresses", List.of(Map.of("street", "address1"), Map.of("street", "address2"))))));
        data.put("data", persons);

        String expectedQuery = "query persons(name: \"t\\\"es\\\"t\", age: 20, addresses: [\"address1\", \"address2\"]) {id name age}";
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
        assertEquals("t\"es\"t", result.getList().get(0).get("personName"));
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
        Request request = new Request("new \"Name\"", 1, 99, List.of(new Address("address1")));

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("createPerson",
                new HashMap<>(Map.of("id", 1,
                        "name", request.getPersonName(),
                        "nameId", request.getNameId(),
                        "age", request.getAge(),
                        "addresses", List.of(Map.of("street", "address1")))));
        data.put("data", persons);

        String expectedQuery = "mutation { createPerson(name: \"new \\\"Name\\\"\", nameId: 1, age: 99, " +
                "addresses: [{street: \"address1\"}]) {id name age address: {street}} }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        SetDataResponse response = restTemplate.postForObject(url, request, SetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals("success", response.getMeta().getAlert().getMessages().get(0).getSeverity());
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

        String expectedQuery = "query persons(sort: { [{name: \"asc\"}], [{age: \"desc\"}] }) { name age }";
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

        expectedQuery = "query persons(sort: { [{name: \"asc\"}] }) { name age }";
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

    @Test
    public void testHierarchicalSelect() {
        String queryPath = "/n2o/data/test/graphql/hierarchicalSelect";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("name", "test", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons() { id price showrooms { id name owner { name inn } } }";
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(DataSet.class)))
                .thenReturn(new DataSet(data));

        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(url, GetDataResponse.class);
        verify(restTemplateMock).postForObject(anyString(), httpEntityCaptor.capture(), eq(DataSet.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> payloadValue = (Map<String, Object>) httpEntityCaptor.getValue().getBody();

        // graphql payload
        assertEquals(expectedQuery, payloadValue.get("query"));
    }

    @Test
    public void testEnums() {
        // MULTIPLE SORTS
        String queryPath = "/n2o/data/test/graphql/enums?sorting.name=ASC&sorting.age=DESC";
        String url = "http://localhost:" + appPort + queryPath;

        // mocked data
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> persons = new HashMap<>();
        persons.put("persons", Collections.singletonList(
                Map.of("name", "test", "age", 20)));
        data.put("data", persons);

        String expectedQuery = "query persons( {nameDirection: ASC, sortProperty: prop}, {ageDirection: DESC, sortProperty: prop} ) { name age }";
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
        queryPath = "/n2o/data/test/graphql/enums?sorting.name=ASC";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query persons( {nameDirection: ASC, sortProperty: prop} ) { name age }";
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
        queryPath = "/n2o/data/test/graphql/enums?sorting.age=ASC";
        url = "http://localhost:" + appPort + queryPath;

        expectedQuery = "query persons( {ageDirection: ASC, sortProperty: prop} ) { name age }";
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
        private Integer nameId;
        private Integer age;
        private List<Address> addresses;

        public Request(String name, Integer age, List<Address> addresses) {
            this.personName = name;
            this.age = age;
            this.addresses = addresses;
        }

        public Request(String name, Integer nameId, Integer age, List<Address> addresses) {
            this.personName = name;
            this.nameId = nameId;
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
