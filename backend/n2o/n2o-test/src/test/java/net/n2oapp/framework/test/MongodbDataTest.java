package net.n2oapp.framework.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = N2oTestApplication.class)
@AutoConfigureDataMongo
public class MongodbDataTest {

    private String collectionName = "user";

    @LocalServerPort
    private int port;
    @Value("${local.mongo.port}")
    private String mongodbPort;
    @Autowired
    private MongoDbDataProviderEngine mongoDbDataProviderEngine;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
        mongoDbDataProviderEngine.setConnectionUrl("mongodb://localhost:" + mongodbPort);
        mongoDbDataProviderEngine.setDatabaseName("test");

        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.getCollection(collectionName).insertMany(TestUserBuilder.testData());
    }

    @Test
    public void testSelect() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&sorting.id=asc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(5));
        DataSet document = result.getList().get(0);
        //simple
        assertThat(document.get("name"), is("Anna"));
        //mapping
        assertThat(document.get("userAge"), is(77));
        //date
        assertThat(document.get("birthday"), is("1941-03-27 00:00:00"));
        //boolean
        assertThat(document.get("vip"), is(true));
        //normalize
        assertThat(document.get("gender.name"), is("women"));
    }

    @Test
    public void testSortingLimitOffset() {
        //one field sort
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=2&page=2&sorting.name=desc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(2));
        DataSet document = result.getList().get(0);
        //simple
        assertThat(document.get("name"), is("Anna"));
        //mapping
        assertThat(document.get("userAge"), is(77));
        //date
        assertThat(document.get("birthday"), is("1941-03-27 00:00:00"));
        //boolean
        assertThat(document.get("vip"), is(true));
        //normalize
        assertThat(document.get("gender.name"), is("women"));
    }

    @Test
    public void testFilters() {

    }
}
