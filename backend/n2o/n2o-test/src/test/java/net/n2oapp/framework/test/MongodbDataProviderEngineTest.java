package net.n2oapp.framework.test;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import org.bson.Document;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static net.n2oapp.framework.boot.ObjectMapperConstructor.dataObjectMapper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тестирование сервиса для выполнения запросов к MongoDb
 */

@Testcontainers
@SpringBootTest(
        classes = TestMongoConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.data.mongodb.database=dbName"})
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MongodbDataProviderEngineTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.10");
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private MongoDbDataProviderEngine engine;

    private N2oMongoDbDataProvider provider;
    @LocalServerPort
    private int appPort;
    private String id;

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    @BeforeAll
    public void init() {
        engine.setMapper(dataObjectMapper());

        //создаем коллекцию
        provider = new N2oMongoDbDataProvider();
        String collectionName = "user";
        provider.setCollectionName(collectionName);
        provider.setDatabaseName("dbName");
        provider.setConnectionUrl("mongodb://" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getFirstMappedPort());

        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.getCollection(collectionName).insertMany(TestUserBuilder.testData());
    }

    @Test
    void testSelect() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&sorting.id=asc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(5));
        DataSet document = result.getList().get(0);
        //simple
        assertThat(document.get("name"), is("Anna"));
        //mapping
        assertThat(document.get("userAge"), is(77));
        //boolean
        assertThat(document.get("vip"), is(true));
        //normalize
        assertThat(document.get("gender.name"), is("women"));
    }

    @Test
    void testSortingLimitOffset() {
        //one field sort
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=2&page=1&sorting.name=asc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(5));
        assertThat(result.getList().size(), is(2));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("Anna"));

        //auto generate sort body
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=2&page=2&sorting.userAge=desc";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        document = result.getList().get(0);
        assertThat(document.get("name"), is("Inna"));

        //todo several sort
    }

    @Test
    void testFilters() {
        String queryPath = "/n2o/data/test/mongodb";
        //eq generate all
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?name=Inna&size=10&page=1";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("Inna"));
        String id = (String) document.get("id");
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id + "&size=10&page=1";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        document = result.getList().get(0);
        assertThat(document.get("id"), is(id));
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?gender_id=1&size=10&page=1";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        document = result.getList().get(0);
        assertThat(document.get("name"), is("Artur"));

        //like + mapping
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&nameLike=nn";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(2));

        //likeStart with body
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&nameStart=Inn";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        assertThat(result.getList().get(0).get("name"), is("Inna"));

        //notEq
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&notName=Inna";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(4));

        //in
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&userAgeIn=77&userAgeIn=9&userAgeIn=266";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(2));

        //in string
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&userNameIn=Inna&userNameIn=Anna";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(2));
        String id1 = result.getList().get(0).getId();
        String id2 = result.getList().get(1).getId();

        //in id
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&idIn=" + id1 + "&idIn=" + id2;
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(2));

        //notIn
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&userAgeNotIn=77&userAgeNotIn=9&userAgeNotIn=23";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(2));

        //more, less
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&birthdayMore=1995-01-01&birthdayLess=2004-01-01";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getPaging().getCount(), is(1));
    }

    @Test
    @Order(1)
    void insertOneOperationTest() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/testInsertMongo";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl,
                new Request(null, "test", 99, "1976-01-03T00:00:00", true, new Gender(1, "Men")), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), CoreMatchers.is("success"));
        id = (String) response.getData().get("id");
        assertThat(id, notNullValue());

        restTemplate = new RestTemplate();
        queryPath = "/n2o/data/test/mongodb";
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id;
        ResponseEntity<GetDataResponse> responseList = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert responseList.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = responseList.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("test"));
        assertThat(document.get("userAge"), is(99));
        assertThat(document.get("gender.name"), is("men"));
        assertThat(document.get("birthday"), is("1976-01-03T00:00:00"));
    }

    @Test
    @Order(2)
    void updateOneOperationTest() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/testUpdateMongo";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl,
                new Request(id, "test22", 99, null, true, new Gender(2, "Women")), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), CoreMatchers.is("success"));

        restTemplate = new RestTemplate();
        queryPath = "/n2o/data/test/mongodb";
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id;
        ResponseEntity<GetDataResponse> responseList = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert responseList.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = responseList.getBody();
        assertThat(result.getPaging().getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("test22"));
        assertThat(document.get("userAge"), is(99));
        assertThat(document.get("gender.name"), is("women"));
    }

    @Test
    @Order(3)
    void deleteOneOperationTest() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/testDeleteMongo";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl,
                new Request(id, null, null, null, null, null), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), CoreMatchers.is("success"));

        restTemplate = new RestTemplate();
        queryPath = "/n2o/data/test/mongodbCount";
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id;
        ResponseEntity<GetDataResponse> responseList = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert responseList.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = responseList.getBody();
        assertThat(result.getPaging().getCount(), is(0));
    }

    @Test
    @Order(4)
    void deleteManyOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.OperationEnum.INSERT_ONE);
        HashMap<String, Object> inParams = new HashMap<>();
        inParams.put("name", "test2");
        String id1 = (String) engine.invoke(provider, inParams);

        inParams = new HashMap<>();
        inParams.put("name", "test3");
        String id2 = (String) engine.invoke(provider, inParams);

        // проверяем, что произошла вставка
        provider.setOperation(N2oMongoDbDataProvider.OperationEnum.FIND);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("{ _id: {$in: #idIn }}")));
        inParams.put("idIn", "[new ObjectId('" + id1 + "'), new ObjectId('" + id2 + "')]");
        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));

        // удаляем
        provider.setOperation(N2oMongoDbDataProvider.OperationEnum.DELETE_MANY);
        inParams = new HashMap<>();
        inParams.put("ids", Arrays.asList(id1, id2));
        engine.invoke(provider, inParams);

        // проверяем, что документы удалены
        provider.setOperation(N2oMongoDbDataProvider.OperationEnum.FIND);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("{ _id: {$in: #idIn }}")));
        inParams.put("idIn", "[new ObjectId('" + id1 + "'), new ObjectId('" + id2 + "')]");
        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(0));
    }

    @Test
    void isNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.OperationEnum.FIND);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("{info:null}")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Tanya"));
        assertThat(documents.get(1).get("name"), is("Valentine"));
    }

    @Test
    void isNotNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.OperationEnum.FIND);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("{ info: {$ne:null}}")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Anna"));
        assertThat(documents.get(1).get("name"), is("Artur"));
        assertThat(documents.get(2).get("name"), is("Inna"));
    }

    @Getter
    @Setter
    public static class Request {
        String id;
        String name;
        Integer age;
        String birthday;
        Boolean vip;
        Gender gender;

        public Request(String id, String name, Integer age, String birthday, Boolean vip, Gender gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.birthday = birthday;
            this.vip = vip;
            this.gender = gender;
        }
    }

    @Getter
    @Setter
    public static class Gender {
        Integer id;
        String name;

        public Gender(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
