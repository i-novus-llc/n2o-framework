package net.n2oapp.framework.engine.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider.PrimaryKeyType;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider.PrimaryKeyType.integer;

/**
 * Тестовый провайдер данных из json файла
 */
public class TestDataProviderEngine implements MapInvocationEngine<N2oTestDataProvider>, ResourceLoaderAware {

    /**
     * Путь к файлу для чтения с диска
     */
    private String pathOnDisk;
    /**
     * Путь к ресурсу в classpath
     */
    private String classpathResourcePath;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final Map<String, List<DataSet>> repository = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> sequences = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper;

    public TestDataProviderEngine() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public Class<? extends N2oTestDataProvider> getType() {
        return N2oTestDataProvider.class;
    }

    @Override
    public Object invoke(N2oTestDataProvider invocation, Map<String, Object> inParams) {
        return execute(invocation, inParams, getData(invocation));
    }

    private Object execute(N2oTestDataProvider invocation,
                           Map<String, Object> inParams,
                           List<DataSet> data) {
        if (invocation.getOperation() == null)
            return findAll(inParams, data);
        switch (invocation.getOperation()) {
            case create:
                return create(invocation, inParams, data);
            case findAll:
                return findAll(inParams, data);
            case findOne:
                return findOne(inParams, data);
            case update:
                return update(invocation, inParams, data);
            case updateMany:
                return updateMany(invocation, inParams, data);
            case updateField:
                return updateField(invocation, inParams, data);
            case delete:
                return delete(invocation, inParams, data);
            case deleteMany:
                return deleteMany(invocation, inParams, data);
            case count:
                return count(inParams, data);
            case echo:
                return inParams;
        }
        throw new N2oException("Unsupported invocation's operation");
    }

    private List<DataSet> findAll(Map<String, Object> inParams, List<DataSet> data) {
        List<String> sortings = (List<String>) inParams.get("sorting");
        List<String> filters = (List<String>) inParams.get("filters");
        Integer limit = (Integer) inParams.get("limit");
        Integer offset = (Integer) inParams.get("offset");
        Integer count = (Integer) inParams.get("count");

        List<DataSet> modifiableData = new ArrayList<>(data);
        modifiableData = filter(filters, inParams, modifiableData);
        sort(sortings, inParams, modifiableData);
        return paginate(limit, offset, count, modifiableData);
    }

    private DataSet findOne(Map<String, Object> inParams, List<DataSet> data) {
        List<String> filters = (List<String>) inParams.get("filters");
        if (filters == null)
            filters = inParams.keySet().stream().map(k -> k + " :eq :" + k).collect(Collectors.toList());
        List<DataSet> modifiableData = new ArrayList<>(data);
        modifiableData = filter(filters, inParams, modifiableData);
        return modifiableData.isEmpty() ? null : modifiableData.get(0);
    }

    private int count(Map<String, Object> inParams,
                      List<DataSet> data) {
        List<String> filters = (List<String>) inParams.get("filters");
        List<DataSet> modifiableData = new ArrayList<>(data);
        return filter(filters, inParams, modifiableData).size();
    }

    private Object create(N2oTestDataProvider invocation,
                          Map<String, Object> inParams,
                          List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList<>(data);
        DataSet newElement = new DataSet();
        newElement.put(invocation.getPrimaryKey(), generateKey(invocation.getPrimaryKeyType(), invocation.getFile()));

        updateElement(newElement, inParams.entrySet());

        modifiableData.add(0, newElement);
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());

        return newElement;
    }

    private Object update(N2oTestDataProvider invocation,
                          Map<String, Object> inParams,
                          List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList<>(data);
        if (inParams.get(invocation.getPrimaryKey()) == null)
            throw new N2oException("Id is required for operation \"update\"");

        DataSet element = modifiableData
                .stream()
                .filter(buildPredicate(invocation.getPrimaryKeyType(), invocation.getPrimaryKey(), inParams))
                .findFirst()
                .orElseThrow(() -> new N2oException("No such element"));

        updateElement(element, inParams.entrySet());
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());

        return modifiableData;
    }

    private Object updateMany(N2oTestDataProvider invocation,
                              Map<String, Object> inParams,
                              List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList<>(data);
        if (inParams.get(invocation.getPrimaryKeys()) == null)
            throw new N2oException("Ids is required for operation \"updateMany\"");
        List<DataSet> elements = modifiableData.stream()
                .filter(buildListPredicate(invocation.getPrimaryKeyType(), invocation.getPrimaryKey(),
                        invocation.getPrimaryKeys(), inParams))
                .collect(Collectors.toList());
        Map<String, Object> fields = new HashMap<>(inParams);
        fields.remove(invocation.getPrimaryKeys());
        for (DataSet element : elements) {
            updateElement(element, fields.entrySet());
        }
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());
        return modifiableData;
    }

    private Object updateField(N2oTestDataProvider invocation,
                               Map<String, Object> inParams,
                               List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList<>(data);
        if (inParams.get(invocation.getPrimaryKey()) == null)
            throw new N2oException("Id is required for operation \"updateField\"");
        if (!inParams.containsKey("key") || !inParams.containsKey("value"))
            throw new N2oException("Should contains parameters \"key\", \"value\" for operation \"updateField\"");

        DataSet element = modifiableData
                .stream()
                .filter(buildPredicate(invocation.getPrimaryKeyType(), invocation.getPrimaryKey(), inParams))
                .findFirst()
                .orElseThrow(() -> new N2oException("No such element"));

        Map<String, Object> fieldData = new HashMap<>();
        fieldData.put(invocation.getPrimaryKey(), inParams.get(invocation.getPrimaryKey()));
        fieldData.put((String) inParams.get("key"), inParams.get("value"));
        if (inParams.containsKey("key2") && inParams.get("key2") != null)
            fieldData.put((String) inParams.get("key2"), inParams.get("value2"));

        updateElement(element, fieldData.entrySet());
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());

        return null;
    }

    private Object delete(N2oTestDataProvider invocation,
                          Map<String, Object> inParams,
                          List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList(data);
        if (inParams.get(invocation.getPrimaryKey()) == null)
            throw new N2oException("Id is required for operation \"delete\"");

        modifiableData.removeIf(buildPredicate(invocation.getPrimaryKeyType(), invocation.getPrimaryKey(), inParams));
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());

        return null;
    }

    private Object deleteMany(N2oTestDataProvider invocation,
                              Map<String, Object> inParams,
                              List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList(data);
        if (inParams.get(invocation.getPrimaryKeys()) == null)
            throw new N2oException("Ids is required for operation \"deleteMany\"");
        modifiableData.removeIf(buildListPredicate(invocation.getPrimaryKeyType(), invocation.getPrimaryKey(),
                invocation.getPrimaryKeys(), inParams));
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());
        return null;
    }


    private static Predicate<DataSet> buildPredicate(PrimaryKeyType primaryKeyType, String primaryKeyFieldId, Map<String, Object> data) {
        if (integer.equals(primaryKeyType))
            return obj -> ((Number) data.get(primaryKeyFieldId)).longValue() == ((Number) obj.get(primaryKeyFieldId)).longValue();
        else
            return obj -> data.get(primaryKeyFieldId).equals(obj.get(primaryKeyFieldId));
    }

    private static Predicate<DataSet> buildListPredicate(PrimaryKeyType primaryKeyType, String primaryKeyFieldId, String primaryKeysFieldId, Map<String, Object> data) {
        if (integer.equals(primaryKeyType)) {
            Set<Long> list = (Set<Long>) ((List) data.get(primaryKeysFieldId)).stream()
                    .map(o -> ((Number) o).longValue()).collect(Collectors.toSet());
            return obj -> list.contains(((Number) obj.get(primaryKeyFieldId)).longValue());
        } else {
            return obj -> ((List<String>) data.get(primaryKeysFieldId)).contains((obj.get(primaryKeyFieldId)));
        }
    }

    private Object generateKey(PrimaryKeyType primaryKeyType, String fileName) {
        if (integer.equals(primaryKeyType)) {
            return sequences.get(richKey(fileName)).incrementAndGet();
        } else {
            return UUID.randomUUID().toString();
        }
    }

    private Map updateElement(Map element, Set<Map.Entry<String, Object>> fields) {
        for (Map.Entry field : fields) {
            if (field.getValue() instanceof Date) {
                element.put(field.getKey(), new SimpleDateFormat("dd.MM.yyy HH:mm:ss").format(field.getValue()));
            } else {
                element.put(field.getKey(), field.getValue());
            }
        }
        return element;
    }

    private List<DataSet> paginate(Integer limit, Integer offset, Integer count, List<DataSet> data) {
        Boolean unique = count != null && count.equals(1);

        if (limit != null && offset != null && !unique) {
            data = data
                    .stream()
                    .limit(limit + offset)
                    .skip(offset).collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> filter(List<String> filters, Map<String, Object> inParams, List<DataSet> data) {
        if (filters == null || filters.isEmpty()) {
            if (inParams != null && inParams.containsKey("id")) {
                String id = "" + inParams.get("id");
                data = data.stream().filter(m -> m.getId().equals(id)).collect(Collectors.toList());
            }
            return data;
        }
        for (String filter : filters) {
            String[] splittedFilter = filter.replace(" ", "").split(":");
            String field = splittedFilter[0];
            Object pattern = inParams.get(splittedFilter[2]);

            switch (splittedFilter[1]) {
                case "eq":
                    data = eqFilterData(field, pattern, data);
                    break;
                case "like":
                    data = likeFilterData(field, pattern, data);
                    break;
                case "in":
                    data = inFilterData(field, pattern, data);
                    break;
                case "more":
                    data = moreFilterData(field, pattern, data);
                    break;
                case "less":
                    data = lessFilterData(field, pattern, data);
                    break;
                case "isNull":
                    data = isNullFilterData(field, data);
                    break;
                case "isNotNull":
                    data = isNotNullFilterData(field, data);
                    break;
                case "eqOrIsNull":
                    data = eqOrIsNullFilterData(field, pattern, data);
                    break;
                default:
                    throw new N2oException("Wrong filter type!");
            }
        }
        return data;
    }

    private List<DataSet> eqFilterData(String field, Object pattern, List<DataSet> data) {
        if (pattern != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.get(field) == null)
                            return false;
                        if (m.get(field) instanceof Number && pattern instanceof Number)
                            return ((Long) ((Number) m.get(field)).longValue()).equals(((Number) pattern).longValue());
                        return m.get(field).toString().equals(pattern.toString());
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> likeFilterData(String field, Object pattern, List<DataSet> data) {
        if (pattern != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.get(field) == null)
                            return false;
                        return m.get(field).toString().toLowerCase()
                                .contains(pattern.toString().toLowerCase());
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> inFilterData(String field, Object pattern, List<DataSet> data) {
        List patterns = pattern instanceof List ? (List) pattern : Arrays.asList(pattern);
        if (patterns != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.get(field) == null)
                            return false;
                        if (m.get(field) instanceof Number) {
                            List<Long> longPatterns = new ArrayList<>();
                            patterns.forEach(p -> longPatterns.add(((Number) p).longValue()));
                            return longPatterns.contains(((Number) m.get(field)).longValue());
                        }
                        for (Object p : patterns) {
                            if (p.toString().equals(m.get(field).toString()))
                                return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> lessFilterData(String field, Object pattern, List<DataSet> data) {
        if (pattern != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.get(field) == null)
                            return false;
                        if (m.get(field) instanceof Number && pattern instanceof Number) {
                            return ((Number) m.get(field)).longValue() < ((Number) pattern).longValue();
                        }
                        if (pattern instanceof LocalDate) {
                            LocalDate date = LocalDate.parse(m.get(field).toString());
                            return date.isEqual((ChronoLocalDate) pattern) || date.isBefore((ChronoLocalDate) pattern);
                        }
                        if (pattern instanceof LocalDateTime) {
                            LocalDateTime dateTime = LocalDateTime.parse(m.get(field).toString());
                            return dateTime.isEqual((ChronoLocalDateTime<?>) pattern) || dateTime.isBefore((ChronoLocalDateTime<?>) pattern);
                        }
                        return m.get(field).toString().compareTo(pattern.toString()) < 0;
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> moreFilterData(String field, Object pattern, List<DataSet> data) {
        if (pattern != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.get(field) == null)
                            return false;
                        if (m.get(field) instanceof Number && pattern instanceof Number) {
                            return ((Long) ((Number) m.get(field)).longValue()).compareTo(((Number) pattern).longValue()) > 0;
                        }
                        if (pattern instanceof LocalDate) {
                            LocalDate date = LocalDate.parse(m.get(field).toString());
                            return date.isEqual((ChronoLocalDate) pattern) || date.isAfter((ChronoLocalDate) pattern);
                        }
                        if (pattern instanceof LocalDateTime) {
                            LocalDateTime dateTime = LocalDateTime.parse(m.get(field).toString());
                            return dateTime.isEqual((ChronoLocalDateTime<?>) pattern) || dateTime.isAfter((ChronoLocalDateTime<?>) pattern);
                        }
                        return m.get(field).toString().compareTo(pattern.toString()) > 0;
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> isNullFilterData(String field, List<DataSet> data) {
        return data.stream()
                .filter(m -> m.containsKey(field) && m.get(field) == null)
                .collect(Collectors.toList());

    }

    private List<DataSet> isNotNullFilterData(String field, List<DataSet> data) {
        return data.stream()
                .filter(m -> m.containsKey(field) && m.get(field) != null)
                .collect(Collectors.toList());
    }

    private List<DataSet> eqOrIsNullFilterData(String field, Object pattern, List<DataSet> data) {
        if (pattern != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.containsKey(field) && m.get(field) == null)
                            return true;
                        if (m.get(field) instanceof Number && pattern instanceof Number)
                            return ((Long) ((Number) m.get(field)).longValue()).equals(((Number) pattern).longValue());
                        return m.get(field).toString().equals(pattern.toString());
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private void sort(List<String> sortings, Map<String, Object> inParams, List<DataSet> data) {
        if (sortings == null || sortings.isEmpty())
            return;

        String sorting = sortings.get(0);
        String[] splittedSorting = sorting.replace(" ", "").split(":");
        String direction = (String) inParams.get(splittedSorting[1]);

        if (data.isEmpty())
            return;

        Comparator comparator = Comparator.comparing(m -> (Comparable) (((LinkedHashMap) m).get(splittedSorting[0])),
                Comparator.nullsLast(Comparator.naturalOrder()));

        if (comparator == null)
            return;

        if (direction.equals("desc"))
            comparator = comparator.reversed();

        data.sort(comparator);
    }

    private synchronized List<DataSet> getData(N2oTestDataProvider invocation) {
        if (invocation.getFile() == null)
            return new ArrayList<>();
        if (getRepositoryData(invocation.getFile()) == null ||
                fileExistsOnDisk(invocation.getFile())) {
            initRepository(invocation);
        }

        return repository.get(richKey(invocation.getFile()));
    }

    private void updateRepository(String key, List<DataSet> newData) {
        repository.put(richKey(key), newData);
    }

    private List<DataSet> getRepositoryData(String key) {
        return repository.get(richKey(key));
    }

    /**
     * Заполняет хранилище данных из файла
     */
    private void initRepository(N2oTestDataProvider invocation) {
        String path = getFullResourcePath(invocation.getFile());

        if (fileExistsOnDisk(invocation.getFile())) {
            path = "file:" + getFullPathOnDisk(invocation.getFile());
        }

        try (InputStream inputStream = resourceLoader.getResource(path).getInputStream()) {
            List<DataSet> data = loadJson(inputStream, invocation.getPrimaryKeyType(), invocation.getPrimaryKey());
            repository.put(richKey(invocation.getFile()), data);
            if (integer.equals(invocation.getPrimaryKeyType())) {
                long maxId = data
                        .stream()
                        .filter(v -> v.get(invocation.getPrimaryKey()) != null)
                        .mapToLong(v -> (Long) v.get(invocation.getPrimaryKey()))
                        .max().orElse(0);
                sequences.put(richKey(invocation.getFile()), new AtomicLong(maxId));
            }

        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

    private List<DataSet> loadJson(InputStream is, PrimaryKeyType primaryKeyType, String primaryKeyFieldId) throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, DataSet.class);
        List<DataSet> dataList = objectMapper.readValue(is, collectionType);
        for (DataSet data : dataList) {
            if (data.containsKey(primaryKeyFieldId) && integer.equals(primaryKeyType))
                data.put(primaryKeyFieldId, ((Number) data.get(primaryKeyFieldId)).longValue());
        }
        return dataList;
    }

    /**
     * Возвращает полный путь к файлу на диске
     *
     * @param filename Имя файла
     */
    public String getFullPathOnDisk(String filename) {
        return pathOnDisk + validateFilename(filename);
    }

    /**
     * Возвращает полный путь к ресурсу в classpath
     *
     * @param filename Имя файла
     */
    public String getFullResourcePath(String filename) {
        String path = "";
        if (classpathResourcePath != null) {
            path = classpathResourcePath;
        }

        return "classpath:" + path + validateFilename(filename);
    }

    /**
     * Проверяет корректность имени файла и
     * исправляет в случае необходимости
     *
     * @param filename Имя файла
     */
    protected String validateFilename(String filename) {
        if (filename != null && !filename.startsWith("/")) {
            filename = "/" + filename;
        }
        return filename;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getPathOnDisk() {
        return pathOnDisk;
    }

    public void setPathOnDisk(String pathOnDisk) {
        this.pathOnDisk = pathOnDisk;
    }

    public String getClasspathResourcePath() {
        return classpathResourcePath;
    }

    public void setClasspathResourcePath(String classpathResourcePath) {
        this.classpathResourcePath = classpathResourcePath;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Проверяет существование файла на диске
     *
     * @param filename Имя файла
     * @return True если файл с заданным именем и путем,
     * указанным в переменной pathOnDisk, существует, false иначе
     */
    private boolean fileExistsOnDisk(String filename) {
        return pathOnDisk != null &&
                new File(getFullPathOnDisk(filename)).isFile();
    }

    /**
     * Обновляет содержимое файла на диске
     *
     * @param filename Имя файла
     */
    private void updateFile(String filename) {
        if (fileExistsOnDisk(filename)) {
            try (FileWriter fileWriter = new FileWriter(getFullPathOnDisk(filename))) {
                String mapAsJson = objectMapper.writeValueAsString(getRepositoryData(filename));
                fileWriter.write(mapAsJson);
            } catch (IOException e) {
                throw new N2oException(e);
            }
        }
    }

    private String richKey(String key) {
        if (pathOnDisk != null) return pathOnDisk + "/" + key;
        if (classpathResourcePath != null) return classpathResourcePath + "/" + key;
        return key;
    }
}
