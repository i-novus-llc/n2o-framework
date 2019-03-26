package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.N2oResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ResponseMessage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование получения данных sql запросом
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true")
public class DataTest {
    @LocalServerPort
    private int port;

    @Test
    public void testJavaDataQuery3() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/java/v3";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assert result.getCount() == 15;
        assert result.getSize() == 10;
        assert result.getPage() == 1;
        assert result.getList().size() == 10;

        //test data by id
        fooResourceUrl = "http://localhost:" + port + queryPath + "?queryId=testJavaQuery3&size=1&page=1&id=3";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assert result.getCount() == 1;
        assert result.getSize() == 1;
        assert result.getPage() == 1;
        assert result.getList().size() == 1;
        assert result.getList().get(0).get("id").equals(3);
        assert result.getList().get(0).get("value").equals("value3");
    }

    @Test
    public void sqlQuery3() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/sql/v3";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(50));
        assertThat(result.getSize(), is(10));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat(result.getList(), isOrdered());

        //test data by id
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=1&page=1&id=3";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(1));
        assertThat(result.getList().get(0).get("id"), is(3));
        assertThat(result.getList().get(0).get("value"), is("+79655000022"));
    }

    @Test
    public void sqlQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/sql/v4";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(50));
        assertThat(result.getSize(), is(10));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat(result.getList(), isOrdered());

        //test data by id
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=1&page=1&id=3";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(1));
        assertThat(result.getList().get(0).get("id"), is(3));
        assertThat(result.getList().get(0).get("value"), is("+79655000022"));
    }

    @Test
    public void testSqlQueryWithValidations() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "http://localhost:" + port + "/n2o/data/test/sql/validation";
        ResponseEntity<GetDataResponse> response;
        try {
            String fooResourceUrl = queryPath + "?name=testName&size=10&page=1&sorting.value=desc";
            response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
            assertThat(false, is(true));
        } catch (HttpClientErrorException e) {
            N2oResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), N2oResponse.class);
            assertThat(resp.getMeta().getMessages().getForm(), is("testTable.filter"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getSeverity(), is("danger"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getText(), is("id is required"));
        }
        try {
            String fooResourceUrl = queryPath + "?id=1&size=10&page=1&sorting.value=desc";
            response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
            assertThat(false, is(true));
        } catch (HttpClientErrorException e) {
            N2oResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), N2oResponse.class);
            assertThat(resp.getMeta().getAlert().getMessages().get(0).getSeverity(), is("danger"));
            assertThat(resp.getMeta().getAlert().getMessages().get(0).getText(), is("Name should be equals 'testName'"));
        }
        String fooResourceUrl = queryPath + "?id=1&name=testName&size=10&page=1&sorting.value=desc";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void restQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "http://localhost:" + port + "/n2o/data/test/rest/v4";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(queryPath + "?size=10&page=2&sorting.value=desc", GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(50));
        assertThat(result.getList().size(), is(10));
        DataSet row = new DataSet().add("id", 10).add("value", "10");
        assertThat(result.getList(), hasItem(row));
        assertThat(result.getList(), isOrdered());

        //test data by id
        response = restTemplate.getForEntity(queryPath + "?size=1&id=3", GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(1));
        assertThat(result.getList().get(0).get("id"), is(3));
        assertThat(result.getList().get(0).get("value"), is("3"));
    }

    @Test
    public void javaSpringQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/java/spring/v4";
        // String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(10));
        assertThat(result.getSize(), is(10));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat((result.getList()).get(0).get("value"), is("value0"));
    }

    @Test
    public void javaStaticQuery4() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/java/static/v4";
        // String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(10));
        assertThat(result.getSize(), is(10));
        assertThat(result.getPage(), is(1));
        assertThat(result.getList().size(), is(10));
        assertThat(result.getList().get(0).get("value"), is("value0"));
    }

    @Test
    public void sqlInvokeWithValidations() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/invoke/action";
        String fooResourceUrl = "http://localhost:" + port + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl, new Request("1", "testName", "testSurname", new Date()), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), is("success"));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            restTemplate.postForObject(fooResourceUrl, new Request(null, "testName", "testSurname", new Date()), ResponseMessage.class);
            assertThat(false, is(true));
        } catch (HttpClientErrorException e) {
            N2oResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), N2oResponse.class);
            assertThat(resp.getMeta().getMessages().getForm(), is("testForm"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getSeverity(), is("danger"));
            assertThat(resp.getMeta().getMessages().getFields().get("id").getText(), is("Id is null"));
        }
        try {
            restTemplate.postForObject(fooResourceUrl, new Request("22", null, "testSurname", new Date()), ResponseMessage.class);
            assertThat(false, is(true));
        } catch (HttpClientErrorException e) {
            N2oResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), N2oResponse.class);
            assertThat(resp.getMeta().getMessages().getForm(), is("testForm"));
            assertThat(resp.getMeta().getMessages().getFields().get("name").getSeverity(), is("danger"));
            assertThat(resp.getMeta().getMessages().getFields().get("name").getText(), is("Name should be testName"));
        }
        try {
            restTemplate.postForObject(fooResourceUrl, new Request("22", "testName", null, new Date()), ResponseMessage.class);
            assertThat(false, is(true));
        } catch (HttpClientErrorException e) {
            N2oResponse resp = objectMapper.readValue(e.getResponseBodyAsByteArray(), N2oResponse.class);
            ResponseMessage responseMessage = resp.getMeta().getAlert().getMessages().get(0);
            assertThat(responseMessage.getSeverity(), is("danger"));
            assertThat(responseMessage.getText(), is("Surname should be equals 'testSurname'"));
        }
    }

    @Test
    public void masterDetailQuery() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/master/1/detail";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(2));
        for (DataSet data : result.getList()) {
            assertThat(data.get("individualId"), is(1));
        }
    }

    @Test
    public void selectedId() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/select";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.value=desc&selectedId=2";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        GetDataResponse result = response.getBody();
        assertThat(result.getList().size(), is(10));
        assertThat(result.getList().get(0).get("id"), is(2));
    }

    @Test
    public void testResolveSubModels() {
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

    @Getter
    public class Request {
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