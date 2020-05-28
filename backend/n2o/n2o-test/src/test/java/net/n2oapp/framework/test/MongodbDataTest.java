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
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=2&page=1&sorting.name=asc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(5));
        assertThat(result.getList().size(), is(2));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("Anna"));

        //auto generate sort body
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=2&page=2&sorting.userAge=desc";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        document = result.getList().get(0);
        assertThat(document.get("name"), is("Inna"));

        //todo several sort
    }

    @Test
    public void testFilters() {
        //eq generate all
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&name=Inna";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("Inna"));

        //like + mapping
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&nameLike=nn";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));

        //likeStart with body
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&nameStart=Inn";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        assertThat(result.getList().get(0).get("name"), is("Inna"));

        //notEq
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&notName=Inna";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(4));

        //in
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&userAgeIn=77&userAgeIn=9&userAgeIn=266";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));

        //notIn
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&userAgeNotIn=77&userAgeNotIn=9&userAgeNotIn=23";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));


/*        //more, less
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&birthdayMore=1920-05-05&birthdayLess=1965-05-05";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));*/
    }
}
