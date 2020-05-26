package net.n2oapp.framework.boot.mongodb;

import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование сервиса для выполнения запросов к MongoDb
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MongoDbDataProviderTestApplication.class)
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoDbDataProviderEngineTest {
    private MongoDbDataProviderEngine engine;
    private N2oMongoDbDataProvider provider;

    private String collectionName = "user";

    @Value("${local.mongo.port}")
    private String port;
    @Autowired
    private MongoTemplate mongoTemplate;

    private String id;


    @BeforeAll
    public void init() {
        engine = new MongoDbDataProviderEngine();
        provider = new N2oMongoDbDataProvider();

        provider.setConnectionUrl("mongodb://localhost:" + port);
        provider.setDatabaseName("test");
        provider.setCollectionName(collectionName);

        mongoTemplate.createCollection(collectionName);
        mongoTemplate.getCollection(collectionName).insertMany(UserBuilder.testData());
    }


    @Test
    @Order(1)
    public void insertOneOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.insertOne);
        HashMap<String, Object> inParams = new HashMap<>();
        inParams.put("name", "test");
        inParams.put("age", 99);
        inParams.put("birthday", "01.01.1900 00:00:00");
        inParams.put("vip", true);
        HashMap<String, Object> info = new HashMap<>();
        HashMap<String, Object> subInfo = new HashMap<>();
        subInfo.put("d", Arrays.asList("e", true, 1));
        info.put("a", Arrays.asList("b", "c", subInfo));
        inParams.put("info", info);

        id = (String) engine.invoke(provider, inParams);

        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", id);

        Document document = ((List<Document>) engine.invoke(provider, inParams)).get(0);
        assertThat(document.get("name"), is("test"));
        assertThat(document.get("age"), is(99));
        assertThat(document.get("birthday"), is("01.01.1900 00:00:00"));
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
        inParams.put("birthday", "01.01.2000 00:00:00");
        inParams.put("vip", false);
        HashMap<String, Object> info = new HashMap<>();
        HashMap<String, Object> subInfo = new HashMap<>();
        subInfo.put("d", Arrays.asList("e2", false, 2));
        info.put("a", Arrays.asList("b2", "c2", "d2", subInfo));
        inParams.put("info", info);

        engine.invoke(provider, inParams);

        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", id);

        Document document = ((List<Document>) engine.invoke(provider, inParams)).get(0);
        assertThat(document.get("name"), is("test2"));
        assertThat(document.get("age"), is(10));
        assertThat(document.get("birthday"), is("01.01.2000 00:00:00"));
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
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", id);

        List documents = (List<Document>) engine.invoke(provider, inParams);
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
        inParams.put("filters", Arrays.asList("id :in :id"));
        inParams.put("id", Arrays.asList(id1, id2));
        List documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));

        // удаляем
        provider.setOperation(N2oMongoDbDataProvider.Operation.deleteMany);
        inParams = new HashMap<>();
        inParams.put("ids", Arrays.asList(id1, id2));
        engine.invoke(provider, inParams);

        // проверяем, что документы удалены
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("id :in :id"));
        inParams.put("id", Arrays.asList(id1, id2));
        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(0));
    }

    @Disabled
    @Test
    public void findOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        Object documents = engine.invoke(provider, new HashMap<>());
    }

    @Test
    public void countDocumentsOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.countDocuments);
        Long count = (Long) engine.invoke(provider, new HashMap<>());
        assertThat(count, is(5L));
    }

    @Disabled
    @Test
    public void paginationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("limit", 2);
        inParams.put("offset", 2);

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void sortingTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("limit", 3);
        inParams.put("offset", 0);
        inParams.put("sorting", Arrays.asList("name :desc"));

        Object documents = engine.invoke(provider, inParams);

    }

    @Disabled
    @Test
    public void eqFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("name :eq :name"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void moreFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :more :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void lessFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :less :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void likeFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :like :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void inFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :in :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void isNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :isNull :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void isNotNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :isNotNull :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }

    @Disabled
    @Test
    public void eqOrIsNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", Arrays.asList("age :eqOrIsNull :age"));
        inParams.put("", "");

        Object documents = engine.invoke(provider, inParams);
    }
}
