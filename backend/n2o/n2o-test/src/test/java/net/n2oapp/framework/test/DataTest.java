package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.rest.ValidationDataResponse;
import net.n2oapp.framework.api.ui.ResponseMessage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование получения данных sql запросом
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true")
class DataTest {

    @LocalServerPort
    private int port;

    @Test
    void testJavaDataQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/java/v4";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assert result.getPaging().getCount() == 15;
        assert result.getPaging().getSize() == 10;
        assert result.getPaging().getPage() == 1;
        assert result.getList().size() == 10;

        //test data by id
        fooResourceUrl = "http://localhost:" + port + queryPath + "?queryId=testJavaQuery4&size=1&page=1&id=3";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assert result.getPaging().getCount() == 1;
        assert result.getPaging().getSize() == 1;
        assert result.getPaging().getPage() == 1;
        assert result.getList().size() == 1;
        assert result.getList().get(0).get("id").equals(3);
        assert result.getList().get(0).get("value").equals("value3");
    }

    @Test
    void sqlQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/sql/v4";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(50));
        assertThat(result.getPaging().getSize(), is(10));
        assertThat(result.getPaging().getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat(result.getList(), isOrdered());

        //test data by id
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=1&page=1&id=3";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        assertThat(result.getPaging().getSize(), is(1));
        assertThat(result.getPaging().getPage(), is(1));
        assertThat(result.getList().size(), is(1));
        assertThat(result.getList().get(0).get("id"), is(3));
        assertThat(result.getList().get(0).get("value"), is("+79655000022"));
    }

    @Test
    void testSqlQueryWithValidations() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "http://localhost:" + port + "/n2o/data/test/sql/validation";
        ResponseEntity<GetDataResponse> response;
        String fooResourceUrl;

        fooResourceUrl = queryPath + "?name=testName&size=10&page=1&sorting.value=desc";
        try {
            restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        } catch (HttpClientErrorException e) {
            GetDataResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), GetDataResponse.class);
            assertThat(resp.getMeta().getMessages().getForm(), is("testTable.filter"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getSeverity(), is("danger"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getText(), is("id is required"));
        }

        fooResourceUrl = queryPath + "?id=1&size=10&page=1&sorting.value=desc";
        try {
            restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        } catch (HttpClientErrorException e) {
            GetDataResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), GetDataResponse.class);
            assertThat(resp.getMeta().getAlert().getMessages().get(0).getSeverity(), is("danger"));
            assertThat(resp.getMeta().getAlert().getMessages().get(0).getText(), is("Name should be equals 'testName'"));
        }

        fooResourceUrl = queryPath + "?id=1&name=testName&size=10&page=1&sorting.value=desc";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void restQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "http://localhost:" + port + "/n2o/data/test/rest/v4";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(queryPath + "?size=10&page=2&sorting.value=desc", GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(50));
        assertThat(result.getList().size(), is(10));
        DataSet row = new DataSet().add("id", 10).add("value", "10");
        assertThat(result.getList(), hasItem(row));
        assertThat(result.getList(), isOrdered());

        //test data by id
        response = restTemplate.getForEntity(queryPath + "?size=1&id=3", GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        assertThat(result.getPaging().getSize(), is(1));
        assertThat(result.getPaging().getPage(), is(1));
        assertThat(result.getList().size(), is(1));
        assertThat(result.getList().get(0).get("id"), is(3));
        assertThat(result.getList().get(0).get("value"), is("3"));
    }

    @Test
    void javaSpringQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/java/spring/v4";
        // String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(10));
        assertThat(result.getPaging().getSize(), is(10));
        assertThat(result.getPaging().getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat((result.getList()).get(0).get("value"), is("value0"));
    }

    @Test
    void javaStaticQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/java/static/v4";
        // String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(10));
        assertThat(result.getPaging().getSize(), is(10));
        assertThat(result.getPaging().getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat(result.getList().get(0).get("value"), is("value0"));
    }

    @Test
    void sqlInvokeWithValidations() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/invoke/action";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl, new Request("1", "testName", "testSurname", new Date()), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), is("success"));

        try {
            restTemplate.postForObject(fooResourceUrl, new Request(null, "testName", "testSurname", new Date()), SetDataResponse.class);
        } catch (HttpClientErrorException e) {
            SetDataResponse resp = mapper.readValue(e.getResponseBodyAsByteArray(), SetDataResponse.class);
            assertThat(resp.getMeta().getMessages().getForm(), is("testForm"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getSeverity(), is("danger"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getText(), is("Id is null"));
        }

        try {
            restTemplate.postForObject(fooResourceUrl, new Request("22", null, "testSurname", new Date()), SetDataResponse.class);
        } catch (HttpClientErrorException e) {
            SetDataResponse resp = mapper.readValue(e.getResponseBodyAsByteArray(), SetDataResponse.class);
            assertThat(resp.getMeta().getMessages().getForm(), is("testForm"));
            assertThat(resp.getMeta().getMessages().getFields().get("name").getSeverity(), is("danger"));
            assertThat(resp.getMeta().getMessages().getFields().get("name").getText(), is("Name should be testName"));
        }

        try {
            restTemplate.postForObject(fooResourceUrl, new Request("22", "testName", null, new Date()), SetDataResponse.class);
        } catch (HttpClientErrorException e) {
            SetDataResponse resp = mapper.readValue(e.getResponseBodyAsByteArray(), SetDataResponse.class);
            ResponseMessage responseMessage = resp.getMeta().getAlert().getMessages().get(0);
            assertThat(responseMessage.getSeverity(), is("danger"));
            assertThat(responseMessage.getText(), is("Surname should be equals 'testSurname'"));
        }
    }

    @Test
    void testFieldConstraintValidation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/validation/create";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ValidationRequest validationRequest = new ValidationRequest("create_ds1", "checkUniqueName", new Request(null, "Больница №1", null, null));
        ValidationDataResponse response = restTemplate.postForObject(fooResourceUrl, validationRequest, ValidationDataResponse.class);
        assertThat(response.getStatus(), is(200));

        try {
            restTemplate.postForObject(fooResourceUrl, validationRequest, ValidationDataResponse.class);
        } catch (HttpServerErrorException e) {
            ValidationDataResponse resp = mapper.readValue(e.getResponseBodyAsByteArray(), ValidationDataResponse.class);
            assertThat(resp.getField(), is("name"));
            assertThat(resp.getId(), is("checkUniqueName"));
            assertThat(resp.getText(), is("Организация с таким именем уже существует в системе"));
            assertThat(resp.getSeverity(), is("danger"));
        }
    }

    @Test
    void masterDetailQuery() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/master/1/detail";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(2));
        for (DataSet data : result.getList()) {
            assertThat(data.get("individualId"), is(1));
        }
    }

    @Test
    void testResolveSubModels() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/subModels";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getList().size(), is(1));
        assertThat(response.getBody().getList().get(0).get("id"), is(11));
        assertThat(((Map) response.getBody().getList().get(0).get("subModel")).get("id"), is(123));
        assertThat(((Map) response.getBody().getList().get(0).get("subModel")).get("name"), is("testName"));
    }

    @Test
    void testAdditionalInfo() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/testAdditionalInfo";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getAdditionalInfo(), instanceOf(Integer.class));
        assertThat(response.getBody().getAdditionalInfo(), is(600));
    }

    @Getter
    public static class Request {
        String id;
        String name;
        String surname;
        Date birthdate;

        public Request(String id, String name, String surname, Date birthdate) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.birthdate = birthdate;
        }
    }

    @Getter
    public static class ValidationRequest {
        String datasourceId;
        String validationId;
        Request data;

        public ValidationRequest(String datasourceId, String validationId, Request data) {
            this.datasourceId = datasourceId;
            this.validationId = validationId;
            this.data = data;
        }
    }

    private Matcher<? super Collection<DataSet>> isOrdered() {
        return new TypeSafeMatcher<Collection<DataSet>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("describe the error has you like more");
            }

            @Override
            protected boolean matchesSafely(Collection<DataSet> item) {
                DataSet prev = null;
                for (DataSet dataSet : item) {
                    if ((prev != null) && (prev.getString("value").compareTo(dataSet.getString("value")) < 0))
                        return false;
                    prev = dataSet;
                }
                return true;
            }
        };
    }

}