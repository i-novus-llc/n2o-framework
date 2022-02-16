package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import net.n2oapp.framework.engine.data.rest.json.RestEngineTimeModule;
import org.bson.Document;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тестирование сервиса для выполнения запросов к MongoDb
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.main.allow-bean-definition-overriding=true", "spring.data.mongodb.database=dbName"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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

    private String id;


    @BeforeAll
    public void init() {
        engine.setMapper(mongoObjectMapper());

        provider = new N2oMongoDbDataProvider();
        provider.setCollectionName(collectionName);
        provider.setDatabaseName("dbName");
        provider.setConnectionUrl("mongodb://localhost:" + port);

        MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(new MongoClientURI("mongodb://localhost:" + port)), "dbName");
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.getCollection(collectionName).insertMany(TestUserBuilder.testData());
    }

    @Test
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
    public void testFilters() {
        String queryPath = "/n2o/data/test/mongodb";
        //eq generate all
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:" + appPort + queryPath + "?name=Inna&size=10&page=1";
        ResponseEntity<GetDataResponse> response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = response.getBody();
        assertThat(result.getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("Inna"));
        String id = (String) document.get("id");
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id + "&size=10&page=1";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        document = result.getList().get(0);
        assertThat(document.get("id"), is(id));
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?gender_id=1&size=10&page=1";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(1));
        document = result.getList().get(0);
        assertThat(document.get("name"), is("Artur"));

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

        //in string
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&userNameIn=Inna&userNameIn=Anna";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(2));
        String id1 = result.getList().get(0).getId();
        String id2 = result.getList().get(1).getId();

        //in id
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&idIn=" + id1 + "&idIn=" + id2;
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

        //more, less
        restTemplate = new RestTemplate();
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?size=10&page=1&birthdayMore=1995-01-01&birthdayLess=2004-01-01";
        response = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert response.getStatusCode().equals(HttpStatus.OK);
        result = response.getBody();
        assertThat(result.getCount(), is(1));
    }

    @Test
    @Order(1)
    public void insertOneOperationTest() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/testInsertMongo";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl,
                new Request(null, "test", 99, "1976-01-03T00:00:00", true, new Gender(1, "Men")), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getColor(), CoreMatchers.is("success"));
        id = (String) response.getData().get("id");
        assertThat(id, notNullValue());

        restTemplate = new RestTemplate();
        queryPath = "/n2o/data/test/mongodb";
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id;
        ResponseEntity<GetDataResponse> responseList = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert responseList.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = responseList.getBody();
        assertThat(result.getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("test"));
        assertThat(document.get("userAge"), is(99));
        assertThat(document.get("gender.name"), is("men"));
        assertThat(document.get("birthday"), is("1976-01-03T00:00:00"));
    }

    @Test
    @Order(2)
    public void updateOneOperationTest() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/testUpdateMongo";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl,
                new Request(id, "test22", 99, null, true, new Gender(2, "Women")), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getColor(), CoreMatchers.is("success"));

        restTemplate = new RestTemplate();
        queryPath = "/n2o/data/test/mongodb";
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id;
        ResponseEntity<GetDataResponse> responseList = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert responseList.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = responseList.getBody();
        assertThat(result.getCount(), is(1));
        DataSet document = result.getList().get(0);
        assertThat(document.get("name"), is("test22"));
        assertThat(document.get("userAge"), is(99));
        assertThat(document.get("gender.name"), is("women"));
    }

    @Test
    @Order(3)
    public void deleteOneOperationTest() {
        RestTemplate restTemplate = new RestTemplate();
        String queryPath = "/n2o/data/testDeleteMongo";
        String fooResourceUrl = "http://localhost:" + appPort + queryPath;
        SetDataResponse response = restTemplate.postForObject(fooResourceUrl,
                new Request(id, null, null, null, null, null), SetDataResponse.class);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getColor(), CoreMatchers.is("success"));

        restTemplate = new RestTemplate();
        queryPath = "/n2o/data/test/mongodbCount";
        fooResourceUrl = "http://localhost:" + appPort + queryPath + "?id=" + id;
        ResponseEntity<GetDataResponse> responseList = restTemplate.getForEntity(fooResourceUrl, GetDataResponse.class);
        assert responseList.getStatusCode().equals(HttpStatus.OK);
        GetDataResponse result = responseList.getBody();
        assertThat(result.getCount(), is(0));
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("{ _id: {$in: #idIn }}")));
        inParams.put("idIn", "[new ObjectId('" + id1 + "'), new ObjectId('" + id2 + "')]");
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("{ _id: {$in: #idIn }}")));
        inParams.put("idIn", "[new ObjectId('" + id1 + "'), new ObjectId('" + id2 + "')]");
        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(0));
    }

    @Test
    public void isNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("{info:null}")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Tanya"));
        assertThat(documents.get(1).get("name"), is("Valentine"));
    }

    @Test
    public void isNotNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
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


    private ObjectMapper mongoObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        RestEngineTimeModule module = new RestEngineTimeModule(new String[]{"yyyy-MM-dd'T'hh:mm:ss.SSSZ"});
        objectMapper.registerModules(module);
        return objectMapper;
    }

    public static String mapIdIn(DataList ids) {
        StringBuilder res = new StringBuilder().append("[");
        for (int i = 0; i < ids.size(); i++)
            res.append("new ObjectId('").append(ids.get(i)).append("'),");
        res.deleteCharAt(res.lastIndexOf(","));
        res.append("]");
        return res.toString();
    }
}
