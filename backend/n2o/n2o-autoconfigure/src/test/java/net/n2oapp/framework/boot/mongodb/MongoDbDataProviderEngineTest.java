package net.n2oapp.framework.boot.mongodb;

import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование сервиса для выполнения запросов к MongoDb
 */

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
        mongoTemplate.getCollection(collectionName).insertMany(TestUserBuilder.testData());
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("id :eq :id")));
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("id :eq :id")));
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("id :eq :id")));
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("id :in :id")));
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
        inParams.put("filters", new ArrayList<>(Arrays.asList("id :in :id")));
        inParams.put("id", Arrays.asList(id1, id2));
        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(0));
    }

    @Test
    public void findOperationTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        List<Document> documents = (List<Document>) engine.invoke(provider, new HashMap<>());

        assertThat(documents.size(), is(5));
        assertThat(documents.get(0).get("name"), is("Сёмина Мария Васильевна"));
        assertThat(documents.get(4).get("name"), is("Ативанова Елена Александровна"));
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
        assertThat(documents.get(0).get("name"), is("Кежватова Анастасия Викторовна"));
        assertThat(documents.get(1).get("name"), is("Чекашкина Людмила Ивановна"));
    }

    @Test
    public void sortingTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("sorting", new ArrayList<>(Arrays.asList("vip :vipDirection", "gender.id :genderDirection", "name :nameDirection")));
        inParams.put("vipDirection", "desc");
        inParams.put("genderDirection", "asc");
        inParams.put("nameDirection", "desc");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(5));
        // vip: true, gender.id: 1
        assertThat(documents.get(0).get("name"), is("Патронов Алексей Иванович"));
        // vip: true, gender.id: 2, name: Ч...
        assertThat(documents.get(1).get("name"), is("Чекашкина Людмила Ивановна"));
        // vip: true, gender.id: 2, name: С...
        assertThat(documents.get(2).get("name"), is("Сёмина Мария Васильевна"));
        // vip: false, gender.id: 3, name: К...
        assertThat(documents.get(3).get("name"), is("Кежватова Анастасия Викторовна"));
        // vip: false, gender.id: 3, name: А...
        assertThat(documents.get(4).get("name"), is("Ативанова Елена Александровна"));

    }

    @Test
    public void eqFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("age :eq :age")));
        inParams.put("age", 23);

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(1));
        assertThat(documents.get(0).get("name"), is("Кежватова Анастасия Викторовна"));
        assertThat(documents.get(0).get("age"), is(23));

        // фильтрация по вложенному полю
        inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("gender.id :eq :gender.id")));
        inParams.put("gender.id", 2);

        documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Сёмина Мария Васильевна"));
        assertThat(documents.get(1).get("name"), is("Чекашкина Людмила Ивановна"));
    }

    @Test
    public void likeFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("name :like :name")));
        inParams.put("name", "ИВАН");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Патронов Алексей Иванович"));
        assertThat(documents.get(1).get("name"), is("Чекашкина Людмила Ивановна"));
        assertThat(documents.get(2).get("name"), is("Ативанова Елена Александровна"));
    }

    @Test
    public void inFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("gender.id :in :gender.id")));
        inParams.put("gender.id", Arrays.asList(1, 2));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Сёмина Мария Васильевна"));
        assertThat(documents.get(1).get("name"), is("Патронов Алексей Иванович"));
        assertThat(documents.get(2).get("name"), is("Чекашкина Людмила Ивановна"));
    }

    @Test
    public void moreFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("birthday :more :birthday")));
        inParams.put("birthday", "1995-01-01");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Патронов Алексей Иванович"));
        assertThat(documents.get(1).get("name"), is("Кежватова Анастасия Викторовна"));
    }

    @Test
    public void lessFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("age :less :age")));
        inParams.put("age", 30);

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Патронов Алексей Иванович"));
        assertThat(documents.get(1).get("name"), is("Кежватова Анастасия Викторовна"));
    }

    @Test
    public void isNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("info :isNull :info")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(2));
        assertThat(documents.get(0).get("name"), is("Чекашкина Людмила Ивановна"));
        assertThat(documents.get(1).get("name"), is("Ативанова Елена Александровна"));
    }

    @Test
    public void isNotNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("info :isNotNull :info")));

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Сёмина Мария Васильевна"));
        assertThat(documents.get(1).get("name"), is("Патронов Алексей Иванович"));
        assertThat(documents.get(2).get("name"), is("Кежватова Анастасия Викторовна"));
    }

    @Test
    public void eqOrIsNullFilterTest() {
        provider.setOperation(N2oMongoDbDataProvider.Operation.find);
        HashMap<Object, Object> inParams = new HashMap<>();
        inParams.put("filters", new ArrayList<>(Arrays.asList("info :eqOrIsNull :info")));
        inParams.put("info", "bbb");

        List<Document> documents = (List<Document>) engine.invoke(provider, inParams);
        assertThat(documents.size(), is(3));
        assertThat(documents.get(0).get("name"), is("Патронов Алексей Иванович"));
        assertThat(documents.get(1).get("name"), is("Чекашкина Людмила Ивановна"));
        assertThat(documents.get(2).get("name"), is("Ативанова Елена Александровна"));
    }
}
