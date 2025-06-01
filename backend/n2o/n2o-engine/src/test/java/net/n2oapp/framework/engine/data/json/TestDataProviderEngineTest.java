package net.n2oapp.framework.engine.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider.OperationEnum.*;
import static net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider.PrimaryKeyTypeEnum.STRING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест {@link TestDataProviderEngine}
 */
class TestDataProviderEngineTest {
    
    @TempDir
    public Path testFolder;
    private File testFile;
    private File emptyFile;

    @BeforeEach
    void prepareJsonFile() throws IOException {
        testFile = Files.createFile(testFolder.resolve("test.json")).toFile();
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("[" +
                "{\"id\":1, \"name\":\"test1\", \"type\":\"1\"}" +
                "]");
        fileWriter.close();

        emptyFile = Files.createFile(testFolder.resolve("test2.json")).toFile();
        fileWriter = new FileWriter(emptyFile);
        fileWriter.write("[]");
        fileWriter.close();
    }

    @Test
    void testInitFromDisk() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Проверка, что после создания json файл содержит ожидаемые данные
        provider.setOperation(FIND_ALL);

        List<Map> result = (List<Map>) engine.invoke(provider, new LinkedHashMap<>());
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(0).get("name"), is("test1"));
        assertThat(result.get(0).get("type"), is("1"));
    }

    @Test
    void testCreateOnFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Добавление новых данных
        provider.setOperation(CREATE);

        Map<String, Object> inParamsForCreate = new LinkedHashMap<>();
        inParamsForCreate.put("id", 9L);
        inParamsForCreate.put("name", "test9");
        inParamsForCreate.put("type", "9");

        engine.invoke(provider, inParamsForCreate);

        //Проверка, что после create, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).get("id"), is(9));
        assertThat(result.get(0).get("name"), is("test9"));
        assertThat(result.get(0).get("type"), is("9"));
        assertThat(result.get(1).get("id"), is(1));
        assertThat(result.get(1).get("name"), is("test1"));
        assertThat(result.get(1).get("type"), is("1"));
    }

    @Test
    void testCreateOnReadonlyFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");
        engine.setReadonly(true);

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Добавление новых данных
        provider.setOperation(CREATE);

        Map<String, Object> inParamsForCreate = new LinkedHashMap<>();
        inParamsForCreate.put("id", 9L);
        inParamsForCreate.put("name", "test9");
        inParamsForCreate.put("type", "9");

        engine.invoke(provider, inParamsForCreate);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);

        //Проверяем, что новые данные не записались в файл
        assertThat(result.size(), is(1));

        provider.setOperation(COUNT);
        Map<String, Object> inParams = Collections.emptyMap();

        Integer resultCount = (Integer) engine.invoke(provider, inParams);
        assertThat(resultCount, is(2));
    }

    @Test
    void testUpdateOnFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Обновление данных
        provider.setOperation(UPDATE);

        Map<String, Object> inParamsForUpdate = new LinkedHashMap<>();
        inParamsForUpdate.put("id", 1L);
        inParamsForUpdate.put("name", "test9");
        inParamsForUpdate.put("type", "9");

        engine.invoke(provider, inParamsForUpdate);

        //Проверка, что после update, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1));
        assertThat(result.get(0).get("name"), is("test9"));
        assertThat(result.get(0).get("type"), is("9"));
    }

    @Test
    void testUpdateFieldOnFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Обновление данных поля
        provider.setOperation(UPDATE_FIELD);

        Map<String, Object> inParamsForUpdate = new LinkedHashMap<>();
        inParamsForUpdate.put("id", 1L);
        inParamsForUpdate.put("key", "name");
        inParamsForUpdate.put("value", "newName");

        engine.invoke(provider, inParamsForUpdate);

        //Проверка, что после updateField, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1));
        assertThat(result.get(0).get("name"), is("newName"));
        assertThat(result.get(0).get("type"), is("1"));
    }

    @Test
    void testUpdateManyOnFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");
        //Добавление новых данных
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("[" +
                "{\"id\":9, \"name\":\"test9\", \"type\":\"9\"}," +
                "{\"id\":8, \"name\":\"test8\", \"type\":\"8\"}," +
                "{\"id\":1, \"name\":\"test1\", \"type\":\"1\"}" +
                "]");
        fileWriter.close();
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());
        //Обновление данных
        provider.setOperation(UPDATE_MANY);

        Map<String, Object> inParamsForUpdate = new LinkedHashMap<>();
        inParamsForUpdate.put("ids", Arrays.asList(1, 8));
        inParamsForUpdate.put("name", "new test");
        engine.invoke(provider, inParamsForUpdate);
        //Проверка, что после update, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);
        assertThat(result.size(), is(3));
        assertThat(result.get(0).get("id"), is(9));
        assertThat(result.get(0).get("name"), is("test9"));
        assertThat(result.get(1).get("id"), is(8));
        assertThat(result.get(1).get("name"), is("new test"));
        assertThat(result.get(2).get("id"), is(1));
        assertThat(result.get(2).get("name"), is("new test"));
    }

    @Test
    void testDeleteOnFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Удаление данных
        provider.setOperation(DELETE);

        Map<String, Object> inParamsForDelete = new LinkedHashMap<>();
        inParamsForDelete.put("id", 1L);

        engine.invoke(provider, inParamsForDelete);

        //Проверка, что после delete, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);

        assertThat(result.size(), is(0));
    }

    @Test
    void testDeleteManyOnFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");
        //Добавление новых данных
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("[" +
                "{\"id\":9, \"name\":\"test9\", \"type\":\"9\"}," +
                "{\"id\":8, \"name\":\"test8\", \"type\":\"8\"}," +
                "{\"id\":1, \"name\":\"test1\", \"type\":\"1\"}" +
                "]");
        fileWriter.close();

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());
        //Удаление нескольких строк данных
        provider.setOperation(DELETE_MANY);

        Map<String, Object> inParamsForDelete = new LinkedHashMap<>();
        inParamsForDelete.put("ids", Arrays.asList(1L, 8L));

        engine.invoke(provider, inParamsForDelete);

        //Проверка, что после delete, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(testFile, collectionType);

        assertThat(result.size(), is(1));
    }

    @Test
    void testFindAllAfterChangeInFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Проверка исходных данных в файле
        List<Map> result = (List<Map>) engine.invoke(provider, new LinkedHashMap<>());
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(0).get("name"), is("test1"));
        assertThat(result.get(0).get("type"), is("1"));

        //Добавление новых данных
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("[" +
                "{\"id\":9, \"name\":\"test9\", \"type\":\"9\"}," +
                "{\"id\":1, \"name\":\"test1\", \"type\":\"1\"}" +
                "]");
        fileWriter.close();

        //Проверка, что после изменения json, новые данные будут возвращены
        result = (List<Map>) engine.invoke(provider, new LinkedHashMap<>());
        assertThat(result.size(), is(2));
        assertThat(result.get(0).get("id"), is(9L));
        assertThat(result.get(0).get("name"), is("test9"));
        assertThat(result.get(0).get("type"), is("9"));
        assertThat(result.get(1).get("id"), is(1L));
        assertThat(result.get(1).get("name"), is("test1"));
        assertThat(result.get(1).get("type"), is("1"));
    }

    @Test
    void testFindOneAfterChangeInFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Проверка исходных данных в файле
        List<Map> result = (List<Map>) engine.invoke(provider, new LinkedHashMap<>());
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(0).get("name"), is("test1"));
        assertThat(result.get(0).get("type"), is("1"));

        //Добавление новых данных
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("[" +
                "{\"id\":9, \"name\":\"test9\", \"type\":\"9\"}," +
                "{\"id\":1, \"name\":\"test1\", \"type\":\"1\"}" +
                "]");
        fileWriter.close();

        //Проверка, что после изменения json, новые данные будут возвращены
        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", 9L);

        provider.setOperation(FIND_ONE);

        Map resultAfterChange = (Map) engine.invoke(provider, inParams);
        assertThat(resultAfterChange.get("id"), is(9L));
        assertThat(resultAfterChange.get("name"), is("test9"));
        assertThat(resultAfterChange.get("type"), is("9"));
    }

    @Test
    void testCountAfterChangeInFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(testFile.getName());

        //Проверка исходных данных в файле
        List<Map> result = (List<Map>) engine.invoke(provider, new LinkedHashMap<>());
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(0).get("name"), is("test1"));
        assertThat(result.get(0).get("type"), is("1"));

        //Добавление новых данных
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("[" +
                "{\"id\":9, \"name\":\"test9\", \"type\":\"9\"}," +
                "{\"id\":8, \"name\":\"test8\", \"type\":\"8\"}," +
                "{\"id\":1, \"name\":\"test1\", \"type\":\"1\"}" +
                "]");
        fileWriter.close();

        //Проверка, что после изменения json, количество записей корректно
        provider.setOperation(COUNT);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("filters", Collections.emptyList());

        Integer resultCount = (Integer) engine.invoke(provider, inParams);
        assertThat(resultCount, is(3));
    }

    @Test
    void testFindAllOperation() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("sorting", new ArrayList<>());
        inParams.put("limit", 10);
        inParams.put("offset", 0);
        inParams.put("page", 1);
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", null);

        List<Map> result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(0).get("name"), is("Мария"));

        assertThat(result.get(9).get("id"), is(5607634L));
        assertThat(result.get(9).get("name"), is("Наталья"));
    }

    @Test
    void testFindOneOperation() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");
        provider.setOperation(FIND_ONE);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", 999);

        Map result = (Map) engine.invoke(provider, inParams);
        assertThat(result.get("id"), is(999L));
    }

    /**
     * Проверка, что при пустом фильтре в findOne будет по умолчанию использоваться :eq
     */
    @Test
    void testFindOneWithoutFilterOperation() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");
        provider.setOperation(FIND_ONE);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("age", 20);
        inParams.put("name", "Олег");

        Map result = (Map) engine.invoke(provider, inParams);
        assertThat(result.get("id"), is(5607657L));
    }

    @Test
    void testCountQuery() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");
        provider.setOperation(COUNT);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("filters", Collections.emptyList());

        Integer result = (Integer) engine.invoke(provider, inParams);
        assertThat(result, is(151));

        // with filters
        inParams.put("filters", Collections.singletonList("age :eq :age"));
        inParams.put("age", 34);

        result = (Integer) engine.invoke(provider, inParams);
        assertThat(result, is(4));
    }

    @Test
    void testOneSizeQuery() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("id", 5607676);
        inParams.put("sorting", new ArrayList<>());
        inParams.put("limit", 1);
        inParams.put("offset", 0);
        inParams.put("page", 1);
        inParams.put("filters", Arrays.asList("id :eq :id"));


        List<Map> result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(5607676L));
        assertThat(result.get(0).get("name"), is("Евгений"));
        assertThat(((Map) result.get(0).get("gender")).get("id"), is(2));
        assertThat(((Map) result.get(0).get("gender")).get("name"), is("Женский"));
    }

    @Test
    void testPagination() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");
        provider.setOperation(FIND_ALL);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("sorting", new ArrayList<>());
        inParams.put("limit", 10);
        inParams.put("offset", 10);
        inParams.put("page", 2);


        List<Map> result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("id"), is(5607635L));
        assertThat(result.get(0).get("name"), is("Адилхан"));

        assertThat(result.get(9).get("id"), is(5607644L));
        assertThat(result.get(9).get("name"), is("Григорий"));
    }

    @Test
    void testSorting() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");

        //Сортировка по возрастанию по строковому полю
        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("sorting", Arrays.asList("name :nameDirection"));
        inParams.put("nameDirection", "asc");
        inParams.put("limit", 10);
        inParams.put("offset", 0);
        inParams.put("page", 1);


        List<Map> result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("id"), is(999L));
        assertThat(result.get(9).get("id"), is(5607771L));

        //Сортировка по убыванию по числовому полю
        inParams.put("sorting", Arrays.asList("id :idDirection"));
        inParams.put("idDirection", "desc");
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.get(0).get("id"), is(5607775L));
        assertThat(result.get(9).get("id"), is(5607766L));
    }

    @Test
    void testFilters() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("sorting", new ArrayList<>());
        inParams.put("filters", Arrays.asList("surname :like :surname"));
        inParams.put("surname", "Арт");
        inParams.put("limit", 10);
        inParams.put("offset", 0);
        inParams.put("page", 1);

        //Фильтр по строке "like"
        List<Map> result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(3));
        assertThat(result.get(0).get("id"), is(5607652L));
        assertThat(result.get(2).get("id"), is(5607769L));

        inParams.put("filters", Arrays.asList("id :more :id"));
        inParams.remove("surname");
        inParams.put("id", 5607774);
        //Фильтр по id "more"
        //Ожидается последняя по id запись в хранилище
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(5607775L));

        inParams.put("filters", Arrays.asList("birthday :more :birthday.begin"));
        inParams.remove("id");
        inParams.put("birthday.begin", LocalDateTime.parse("2018-01-18T00:00:00"));
        //Фильтр по birthday "more"
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(5607640L));
        assertThat(result.get(0).get("birthday"), is("2018-01-18T00:00:00Z"));

        inParams.put("filters", Arrays.asList("birthday :less :birthday.end"));
        inParams.remove("birthday.begin");
        inParams.put("birthday.end", LocalDateTime.parse("1927-01-01T00:00:00"));
        //Фильтр по birthday "less"
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(5607677L));
        assertThat(result.get(0).get("birthday"), is("1927-01-01T00:00:00+07:00"));


        inParams.put("filters", Arrays.asList("id :less :id"));
        inParams.remove("birthday.end");
        inParams.put("id", 2);
        //Фильтр по id "less"
        //Ожидается первая по id запись в хранилище
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));

        inParams.put("filters", Arrays.asList("id :in :id"));
        inParams.put("id", Arrays.asList(1, 999, 5607775, 999999999));
        //Фильтр по id "in"
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(3));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(1).get("id"), is(999L));
        assertThat(result.get(2).get("id"), is(5607775L));
        inParams.put("filters", Arrays.asList("name :in :name"));
        inParams.put("name", "Денис");
        //Фильтр по name "in" (проверка одиночного значения)
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("name"), is("Денис"));

        inParams.put("filters", Arrays.asList("id :notIn :id"));
        inParams.put("id", Arrays.asList(999, 5607628, 5607628));
        //Фильтр по id "notIn"
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(1).get("id"), is(5607627L));
        assertThat(result.get(2).get("id"), is(5607629L));
        inParams.put("filters", Arrays.asList("name :notIn :name"));
        inParams.put("name", "Мария");
        //Фильтр по name "notIn" (проверка одиночного значения)
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("name"), is("Test20121026"));

        inParams.put("filters", Arrays.asList("groups.id :in :groups.id"));
        inParams.put("groups.id", Arrays.asList(12));
        //Фильтр по паттерну *.* "in"
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(1).get("id"), is(5607628L));

        //Фильтр по "isNull"
        inParams.put("filters", Arrays.asList("age :isNull :age"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(5607771L));

        //Фильтр по "isNotNull"
        inParams.put("filters", Arrays.asList("newAge :isNotNull :age"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(5607771L));

        //Фильтр по "eqOrIsNull"
        inParams.put("filters", Arrays.asList("age :eqOrIsNull :age"));
        inParams.put("age", 16);
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).get("id"), is(5607655L));
        assertThat(result.get(1).get("id"), is(5607771L));

        //Фильтр по "notEq" age
        inParams.put("filters", Arrays.asList("age :notEq :age"));
        inParams.put("age", 77);
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("id"), is(999L));
        assertThat(result.get(1).get("id"), is(5607627L));
        //Фильтр по "notEq" surname
        inParams.put("filters", Arrays.asList("surname :notEq :surname"));
        inParams.put("surname", "Test20121026");
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(10));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(1).get("id"), is(5607627L));

        //Фильтр "contains" по "groups.id"
        inParams.put("filters", Arrays.asList("groups.id :contains :groups*.id"));
        inParams.put("groups*.id", new ArrayList<>(Arrays.asList(12, 14)));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).get("id"), is(1L));
        assertThat(result.get(1).get("id"), is(5607628L));

        //Фильтр "contains" по "types"
        inParams.put("filters", Arrays.asList("types :contains :types"));
        inParams.put("types", new ArrayList<>(Arrays.asList(1,2)));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).get("id"), is(999L));
        assertThat(result.get(1).get("id"), is(5607627L));
    }

    @Test
    void testFiltersLongValues() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testLongValues.json");

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("sorting", new ArrayList<>());
        inParams.put("filters", Arrays.asList("id :eq :id"));
        inParams.put("id", 1);
        inParams.put("limit", 10);
        inParams.put("offset", 0);
        inParams.put("page", 1);

        //Фильтр по id "eq"
        //запись типа Integer
        List<Map> result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));

        //Фильтр по id "eq"
        //запись типа Long
        inParams.put("id", 9223372036854775807L);
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(9223372036854775807L));

        //Фильтр по date "eq"
        //запись типа Date
        inParams.put("date", "02.03.2000 00:00:00");
        inParams.put("filters", Arrays.asList("date :eq :date"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("date"), is("02.03.2000 00:00:00"));

        //Фильтр по id "less"
        inParams.remove("date");
        inParams.put("id", 9223372036854775807L);
        inParams.put("filters", Arrays.asList("id :less :id"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));

        //Фильтр по id "more"
        inParams.put("id", 1);
        inParams.put("filters", Arrays.asList("id :more :id"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(9223372036854775807L));

        //Фильтр по id "id"
        inParams.put("id", Arrays.asList(9223372036854775807L));
        inParams.put("filters", Arrays.asList("id :in :id"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(9223372036854775807L));

        //Фильтр по id "id"
        inParams.put("id", Arrays.asList(1));
        inParams.put("filters", Arrays.asList("id :in :id"));
        result = (List<Map>) engine.invoke(provider, inParams);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1L));
    }

    @Test
    void testEchoOperation() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setOperation(ECHO);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("test", 1);


        Map result = (Map) engine.invoke(provider, inParams);
        assertThat(result.get("test"), is(1));
    }

    @Test
    void testCreateWithNumericPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");
        provider.setOperation(CREATE);

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("name", "test");
        inParams.put("gender.id", 2);
        inParams.put("gender.name", "Женский");
        inParams.put("vip", true);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);
        inParams.put("birthday", c.getTime());

        Map result = (Map) engine.invoke(provider, inParams);

        assertThat(result.get("id"), is(5607776L));
        assertThat(result.get("name"), is("test"));
        assertTrue((Boolean) result.get("vip"));
        assertThat(((Map) result.get("gender")).get("id"), is(2));
        assertThat(((Map) result.get("gender")).get("name"), is("Женский"));
        assertThat(result.get("birthday"), is(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(0))));

        //Проверка, что после создания элемент появился в хранилище
        provider.setOperation(FIND_ALL);
        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("id", 5607776L);
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 1);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("id :eq :id"));
        List<Map> readingResult = (List<Map>) engine.invoke(provider, inParams);

        assertEquals(readingResult.get(0), result);
    }

    @Test
    void testUpdateWithNumericPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("id", 5607676);
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 1);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("id :eq :id"));

        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Евгений"));
        assertThat(result.get(0).get("birthday"), is("1957-01-01T00:00:00"));
        assertTrue((Boolean) result.get(0).get("vip"));
        assertThat(((Map) result.get(0).get("gender")).get("id"), is(2));
        assertThat(((Map) result.get(0).get("gender")).get("name"), is("Женский"));


        provider.setOperation(UPDATE);
        Map<String, Object> inParamsForUpdate = new LinkedHashMap<>();
        inParamsForUpdate.put("id", 5607676L);
        inParamsForUpdate.put("name", "test");
        inParamsForUpdate.put("gender.id", 1);
        inParamsForUpdate.put("gender.name", "Мужской");
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);

        inParamsForUpdate.put("birthday", c.getTime());
        inParamsForUpdate.put("vip", false);

        Map updatedResult = (Map) engine.invoke(provider, inParamsForUpdate);
        assertThat(updatedResult.get("name"), is("test"));
        assertThat(updatedResult.get("gender.id"), is(1));
        assertThat(updatedResult.get("gender.name"), is("Мужской"));
        assertThat(updatedResult.get("vip"), is(false));

        provider.setOperation(FIND_ALL);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("test"));
        assertThat(result.get(0).get("birthday"), is(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(0))));
        assertFalse((Boolean) result.get(0).get("vip"));
        assertThat(((Map) result.get(0).get("gender")).get("id"), is(1));
        assertThat(((Map) result.get(0).get("gender")).get("name"), is("Мужской"));
    }

    @Test
    void testUpdateFieldWithNumericPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testNumericPrimaryKey.json");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("id", 5607629);
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 1);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("id :eq :id"));

        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Людмила"));

        // изменяем поле name
        provider.setOperation(UPDATE_FIELD);
        Map<String, Object> inParamsForUpdateField = new LinkedHashMap<>();
        inParamsForUpdateField.put("id", 5607629L);
        inParamsForUpdateField.put("key", "name");
        inParamsForUpdateField.put("value", "Ольга");
        engine.invoke(provider, inParamsForUpdateField);

        provider.setOperation(FIND_ALL);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Ольга"));
    }

    @Test
    void testDeleteWithNumericPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        Map<String, Object> inParamsForRead = new LinkedHashMap<>();

        provider.setFile("testNumericPrimaryKey.json");
        provider.setOperation(FIND_ALL);

        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 151);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);

        //Проверка, что до удаления элемент существует
        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        result = result.stream().filter(map -> map.get("id").equals(5607676L)).toList();
        assertThat(result.size(), is(1));


        Map<String, Object> inParamsForDelete = new LinkedHashMap<>();
        inParamsForDelete.put("id", 5607676);
        provider.setOperation(DELETE);
        engine.invoke(provider, inParamsForDelete);

        //Проверка, что удаление прошло успешно
        provider.setOperation(null);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        result = result.stream().filter(map -> map.get("id").equals(5607676)).toList();
        assertThat(result.size(), is(0));
    }

    @Test
    void testCreateWithStringPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setPrimaryKeyType(STRING);
        provider.setFile("testStringPrimaryKey.json");
        provider.setOperation(CREATE);
        provider.setPrimaryKey("testId");

        Map<String, Object> inParams = new LinkedHashMap<>();
        inParams.put("name", "test");
        inParams.put("gender.id", 2);
        inParams.put("gender.name", "Женский");
        inParams.put("vip", true);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);
        inParams.put("birthday", c.getTime());

        Map result = (Map) engine.invoke(provider, inParams);

        assertFalse(((String) result.get("testId")).isEmpty());
        assertThat(result.get("name"), is("test"));
        assertTrue((Boolean) result.get("vip"));
        assertThat(((Map) result.get("gender")).get("id"), is(2));
        assertThat(((Map) result.get("gender")).get("name"), is("Женский"));
        assertThat(result.get("birthday"), is(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(0))));

        //Проверка, что после создания элемент появился в хранилище
        provider.setOperation(FIND_ALL);
        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("id", 5607776L);
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 1);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("id :eq :id"));
        List<Map> readingResult = (List<Map>) engine.invoke(provider, inParams);

        assertEquals(readingResult.get(0), result);
    }

    @Test
    void testUpdateWithStringPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setPrimaryKeyType(STRING);
        provider.setPrimaryKey("testId");
        provider.setFile("testStringPrimaryKey.json");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("testId", "abcd");
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 1);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("testId :eq :testId"));

        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Мария"));
        assertThat(result.get(0).get("birthday"), is("27.03.1941 00:00:00"));
        assertTrue((Boolean) result.get(0).get("vip"));
        assertThat(((Map) result.get(0).get("gender")).get("id"), is(2));
        assertThat(((Map) result.get(0).get("gender")).get("name"), is("Женский"));


        provider.setOperation(UPDATE);
        Map<String, Object> inParamsForUpdate = new LinkedHashMap<>();
        inParamsForUpdate.put("testId", "abcd");
        inParamsForUpdate.put("name", "test");
        inParamsForUpdate.put("gender.id", 1);
        inParamsForUpdate.put("gender.name", "Мужской");
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);

        inParamsForUpdate.put("birthday", c.getTime());
        inParamsForUpdate.put("vip", false);

        Map updatedResult = (Map) engine.invoke(provider, inParamsForUpdate);
        assertThat(updatedResult.get("name"), is("test"));
        assertThat(updatedResult.get("gender.id"), is(1));
        assertThat(updatedResult.get("gender.name"), is("Мужской"));
        assertThat(updatedResult.get("vip"), is(false));

        provider.setOperation(FIND_ALL);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("test"));
        assertThat(result.get(0).get("birthday"), is(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(0))));
        assertFalse((Boolean) result.get(0).get("vip"));
        assertThat(((Map) result.get(0).get("gender")).get("id"), is(1));
        assertThat(((Map) result.get(0).get("gender")).get("name"), is("Мужской"));
    }

    @Test
    void testUpdateManyWithStringPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setPrimaryKeyType(STRING);
        provider.setPrimaryKey("testId");
        provider.setFile("testStringPrimaryKey.json");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("testId", Arrays.asList("a7e0973e-5dfc-4f77-8e1b-2c284d70453d", "a8bd38d9-6784-4764-9e28-acc3f1107f67", "c4d954d4-496b-4777-af57-8827f8018f09"));
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 3);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("testId :in :testId"));


        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Test20121026"));
        assertThat(result.get(0).get("birthday"), is("26.10.2012 00:00:00"));
        assertThat(result.get(1).get("name"), is("Алексей"));
        assertThat(result.get(1).get("birthday"), is("10.05.2009 00:00:00"));
        assertThat(result.get(2).get("name"), is("Анастасия"));
        assertThat(result.get(2).get("birthday"), is("08.04.1995 00:00:00"));

        provider.setOperation(UPDATE_MANY);
        Map<String, Object> inParamsForUpdate = new LinkedHashMap<>();
        inParamsForUpdate.put("testIds", Arrays.asList("c4d954d4-496b-4777-af57-8827f8018f09", "a7e0973e-5dfc-4f77-8e1b-2c284d70453d"));
        inParamsForUpdate.put("name", "new test");

        engine.invoke(provider, inParamsForUpdate);

        provider.setOperation(FIND_ALL);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("new test"));
        assertThat(result.get(0).get("birthday"), is("26.10.2012 00:00:00"));
        assertThat(result.get(1).get("name"), is("Алексей"));
        assertThat(result.get(1).get("birthday"), is("10.05.2009 00:00:00"));
        assertThat(result.get(2).get("name"), is("new test"));
        assertThat(result.get(2).get("birthday"), is("08.04.1995 00:00:00"));
    }

    @Test
    void testUpdateFieldWithStringPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setPrimaryKeyType(STRING);
        provider.setPrimaryKey("testId");
        provider.setFile("testStringPrimaryKey.json");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("testId", "37128f45-e004-450e-b1c5-6afd2adf593c");
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 1);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("testId :eq :testId"));

        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Наталья"));

        // изменяем поле name
        provider.setOperation(UPDATE_FIELD);
        Map<String, Object> inParamsForUpdateField = new LinkedHashMap<>();
        inParamsForUpdateField.put("testId", "37128f45-e004-450e-b1c5-6afd2adf593c");
        inParamsForUpdateField.put("key", "name");
        inParamsForUpdateField.put("value", "Ольга");
        engine.invoke(provider, inParamsForUpdateField);

        provider.setOperation(FIND_ALL);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("Ольга"));
    }

    @Test
    void testDeleteWithStringPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile("testStringPrimaryKey.json");
        provider.setOperation(FIND_ALL);
        provider.setPrimaryKeyType(STRING);
        provider.setPrimaryKey("testId");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 151);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);

        //Проверка, что до удаления элемент существует
        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        result = result.stream().filter(map -> map.get("testId").equals("a7e0973e-5dfc-4f77-8e1b-2c284d70453d")).toList();
        assertThat(result.size(), is(1));


        Map<String, Object> inParamsForDelete = new LinkedHashMap<>();
        inParamsForDelete.put("testId", "a7e0973e-5dfc-4f77-8e1b-2c284d70453d");
        provider.setOperation(DELETE);
        engine.invoke(provider, inParamsForDelete);

        //Проверка, что удаление прошло успешно
        provider.setOperation(null);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        result = result.stream().filter(map -> map.get("testId").equals("a7e0973e-5dfc-4f77-8e1b-2c284d70453d")).toList();
        assertThat(result.size(), is(0));
    }

    @Test
    void testDeleteManyWithStringPK() {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setPrimaryKeyType(STRING);
        provider.setPrimaryKey("testId");
        provider.setFile("testStringPrimaryKey.json");

        Map<String, Object> inParamsForRead = new LinkedHashMap<>();
        List<String> ids = Arrays.asList("a4ead8c6-f3f1-46c3-8b48-7a6085e8cc08", "898a913d-9a28-4cac-b270-cd191cdc84ba");
        inParamsForRead.put("testId", ids);
        inParamsForRead.put("sorting", new ArrayList<>());
        inParamsForRead.put("limit", 2);
        inParamsForRead.put("offset", 0);
        inParamsForRead.put("page", 1);
        inParamsForRead.put("filters", Arrays.asList("testId :in :testId"));


        List<Map> result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.get(0).get("name"), is("НИКОЛАЙ"));
        assertThat(result.get(1).get("name"), is("АНАСТАСИЯ"));

        provider.setOperation(DELETE_MANY);
        Map<String, Object> inParamsForDeleteMany = new LinkedHashMap<>();
        inParamsForDeleteMany.put("testIds", ids);
        engine.invoke(provider, inParamsForDeleteMany);

        provider.setOperation(FIND_ALL);
        result = (List<Map>) engine.invoke(provider, inParamsForRead);
        assertThat(result.size(), is(0));
    }

    @Test
    void testCreateOnEmptyFile() throws IOException {
        TestDataProviderEngine engine = new TestDataProviderEngine();
        engine.setResourceLoader(new DefaultResourceLoader());
        engine.setPathOnDisk(testFolder.toAbsolutePath() + "/");

        N2oTestDataProvider provider = new N2oTestDataProvider();
        provider.setFile(emptyFile.getName());

        //Добавление новых данных в пустой файл
        provider.setOperation(CREATE);

        Map<String, Object> inParamsForCreate = new LinkedHashMap<>();
        inParamsForCreate.put("name", "test10");
        inParamsForCreate.put("type", "10");

        engine.invoke(provider, inParamsForCreate);

        //Проверка, что после create, json файл содержит ожидаемые данные
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, HashMap.class);
        List<Map> result = objectMapper.readValue(emptyFile, collectionType);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).get("id"), is(1));
        assertThat(result.get(0).get("name"), is("test10"));
        assertThat(result.get(0).get("type"), is("10"));
    }
}
