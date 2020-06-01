package net.n2oapp.framework.test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование сервиса для выполнения запросов к MongoDb
 */
@Import(N2oMongoAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = N2oTestApplication.class)
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongodbDataProviderEngineTest {
    @Autowired
    private MongoDbDataProviderEngine engine;
    private N2oMongoDbDataProvider provider;

    private String collectionName = "user";

    @LocalServerPort
    private int appPort;

    @Value("${local.mongo.port}")
    private String port;
    @Autowired
    private MongoTemplate mongoTemplate;

    private String id;


    @BeforeAll
    public void init() {
        engine.setConnectionUrl("mongodb://localhost:" + port);
        engine.setDatabaseName("dbName");

        provider = new N2oMongoDbDataProvider();
        provider.setCollectionName(collectionName);

        MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(new MongoClientURI("mongodb://localhost:" + port)), "dbName");
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.getCollection(collectionName).insertMany(TestUserBuilder.testData());
    }

    @Test
    @Disabled
    public void testSelect() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&sorting.id=asc";
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
        assertThat(document.get("birthday"), is("1941-03-27"));
        //boolean
        assertThat(document.get("vip"), is(true));
        //normalize
        assertThat(document.get("gender.name"), is("women"));
    }

    @Test
    @Disabled
    public void testSortingLimitOffset() {
        //one field sort
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=2&page=1&sorting.name=asc";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(5));
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
    @Disabled
    public void testFilters() {
        //eq generate all
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/test/mongodb";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?name=Inna&size=10&page=1";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("Inna"));
        String id = (String) document.get("id");

        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id="+id + "&size=10&page=1";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        assertThat(document.get("id"), is(id));

        //like + mapping
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&nameLike=nn";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));

        //likeStart with body
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&nameStart=Inn";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        assertThat(result.getList().get(0).get("name"), is("Inna"));

        //notEq
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&notName=Inna";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(4));

        //in
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&userAgeIn=77&userAgeIn=9&userAgeIn=266";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));

        //notIn
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&userAgeNotIn=77&userAgeNotIn=9&userAgeNotIn=23";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));

/*        //todo more, less
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + port + queryPath + "?size=10&page=1&birthdayMore=1920-05-05&birthdayLess=1965-05-05";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));*/
    }


    @Test
    @Order(1)
    public void insertOneOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.insertOne);
        HashMap<String, Object> inParams = new HashMap<>();
        inParams.put("name", "test");
        inParams.put("age", 99);
        inParams.put("birthday", "1900-01-01");
        inParams.put("vip", true);
        HashMap<String, Object> info = new HashMap<>();
        HashMap<String, Object> subInfo = new HashMap<>();
        subInfo.put("d", Arrays.asList("e", true, 1));
        info.put("a", Arrays.asList("b", "c", subInfo));
        inParams.put("info", info);

        id = (String) engine.invoke(provider, inParams);

        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("_id :eq :id")));
        inParams.put("id", id);

        Document document = ((List<Document>) engine.invoke(provider, inParams)).get(0);
        assertThat(document.get("name"), is("test"));
        assertThat(document.get("age"), is(99));
        assertThat(document.get("birthday"), is("1900-01-01"));
        assertThat(document.get("vip"), is(true));

        Document expSubInfo = new Document(subInfo);
        Map<String, Object> expInfoMap = new HashMap<>();
        expInfoMap.put("a", Arrays.asList("b", "c", expSubInfo));
        Document expInfo = new Document(expInfoMap);
        assertThat(document.get("info"), is(expInfo));
    }

    @Test
    @Order(2)
    public void updateOneOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.updateOne);
        HashMap<String, Object> inParams = new HashMap<>();

        inParams.put("id", id);
        inParams.put("name", "test2");
        inParams.put("age", 10);
        inParams.put("birthday", "2000-01-01");
        inParams.put("vip", false);
        HashMap<String, Object> info = new HashMap<>();
        HashMap<String, Object> subInfo = new HashMap<>();
        subInfo.put("d", Arrays.asList("e2", false, 2));
        info.put("a", Arrays.asList("b2", "c2", "d2", subInfo));
        inParams.put("info", info);

        engine.invoke(provider, inParams);

        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("_id :eq :id")));
        inParams.put("id", id);

        Document document = ((List<Document>) engine.invoke(provider, inParams)).get(0);
        assertThat(document.get("name"), is("test2"));
        assertThat(document.get("age"), is(10));
        assertThat(document.get("birthday"), is("2000-01-01"));
        assertThat(document.get("vip"), is(false));

        Document expSubInfo = new Document(subInfo);
        Map<String, Object> expInfoMap = new HashMap<>();
        expInfoMap.put("a", Arrays.asList("b2", "c2", "d2", expSubInfo));
        Document expInfo = new Document(expInfoMap);
        assertThat(document.get("info"), is(expInfo));
    }

    @Test
    @Order(3)
    public void deleteOneOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.deleteOne);
        HashMap<String, Object> inParams = new HashMap<>();

        inParams.put("id", id);
        engine.invoke(provider, inParams);

        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("_id :eq :id")));
        inParams.put("id", id);

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(0));

        id = null;
    }

    @Test
    @Order(4)
    public void deleteManyOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.insertOne);
        HashMap<String, Object> inParams = new HashMap<>();
        inParams.put("name", "test2");
        String id1 = (String) engine.invoke(provider, inParams);

        inParams = new HashMap<>();
        inParams.put("name", "test3");
        String id2 = (String) engine.invoke(provider, inParams);

        // проверяем, что произошла вставка
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("_id :in :id")));
        inParams.put("id", Arrays.asList(id1, id2));
        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));

        // удаляем
        provider.setOperation(N2oMongoDbDataProvider.Operation.deleteMany);
        inParams = new HashMap<>();
        inParams.put("ids", Arrays.asList(id1, id2));
        engine.invoke(provider, inParams);

        // проверяем, что документы удалены
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("_id :in :id")));
        inParams.put("id", Arrays.asList(id1, id2));
        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(0));
    }

    @Test
    public void findOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        List<Document> documents = (List<Document>) engine.invoke(provider, new HashMap<>());

        assertThat(documents.size(), is(5));
        assertThat(documents.get(0).get("name"), is("Anna"));
        assertThat(documents.get(4).get("name"), is("Valentine"));
    }

    @Test
    public void countDocumentsOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.countDocuments);
        Integer count = (Integer) engine.invoke(provider, new HashMap<>());
        assertThat(count, is(5));
    }

    @Test
    public void paginationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("limit", 2);
        inParams.put("offset", 2);

        // 3 и 4 запись
        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Inna"));
        assertThat(documents.get(1).get("name"), is("Tanya"));
    }

   @Test
    public void sortingTest() {
 /*        //todo
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("sorting", new ArrayList<>(Arrays.asList("vip :vipDirection", "gender.id :genderDirection", "name :nameDirection")));
        inParams.put("vipDirection", "desc");
        inParams.put("genderDirection", "asc");
        inParams.put("nameDirection", "desc");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(5));
        // vip: true, gender.id: 1
        assertThat(documents.get(0).get("name"), is("Artur"));
        // vip: true, gender.id: 2, name: Ч...
        assertThat(documents.get(1).get("name"), is("Tanya"));
        // vip: true, gender.id: 2, name: С...
        assertThat(documents.get(2).get("name"), is("Anna"));
        // vip: false, gender.id: 3, name: К...
        assertThat(documents.get(3).get("name"), is("Inna"));
        // vip: false, gender.id: 3, name: А...
        assertThat(documents.get(4).get("name"), is("Valentine"));*/
    }

    @Test
    public void eqFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("age :eq :age")));
        inParams.put("age", 23);

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(1));
        assertThat(documents.get(0).get("name"), is("Inna"));
        assertThat(documents.get(0).get("age"), is(23));

        // фильтрация по вложенному полю
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("gender.id :eq :gender.id")));
        inParams.put("gender.id", 2);

        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Anna"));
        assertThat(documents.get(1).get("name"), is("Tanya"));
    }

    @Test
    public void likeFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("name :like :name")));
        inParams.put("name", "nn");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Anna"));
        assertThat(documents.get(1).get("name"), is("Inna"));
    }

    @Test
    public void inFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("gender.id :in :gender.id")));
        inParams.put("gender.id", Arrays.asList(1, 2));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Anna"));
        assertThat(documents.get(1).get("name"), is("Artur"));
        assertThat(documents.get(2).get("name"), is("Tanya"));
    }

    @Test
    public void moreFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("birthday :more :birthday")));
        inParams.put("birthday", "1995-01-01");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Artur"));
        assertThat(documents.get(1).get("name"), is("Inna"));
    }

    @Test
    public void lessFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("age :less :age")));
        inParams.put("age", 30);

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Artur"));
        assertThat(documents.get(1).get("name"), is("Inna"));
    }

    @Test
    public void isNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("info :isNull :info")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Tanya"));
        assertThat(documents.get(1).get("name"), is("Valentine"));
    }

    @Test
    public void isNotNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("info :isNotNull :info")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Anna"));
        assertThat(documents.get(1).get("name"), is("Artur"));
        assertThat(documents.get(2).get("name"), is("Inna"));
    }

    @Test
    public void eqOrIsNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("info :eqOrIsNull :info")));
        inParams.put("info", "bbb");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Artur"));
        assertThat(documents.get(1).get("name"), is("Tanya"));
        assertThat(documents.get(2).get("name"), is("Valentine"));
    }

}
