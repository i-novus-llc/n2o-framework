package net.n2oapp.framework.engine.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.NestedList;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider.PrimaryKeyType;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
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

    /**
     * Обновление данных в файле на диске
     */
    private boolean readonly;


    private final Map<String, List<DataSet>> repository = new ConcurrentHashMap<>();
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final Map<String, AtomicLong> sequences = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper;

    public TestDataProviderEngine() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
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

    public void deleteSessionDataSets(HttpSession session) {
        for (Map.Entry<String, List<DataSet>> entry : repository.entrySet()) {
            if (session.getId().equals(entry.getKey().split("/")[0]))
                repository.remove(entry.getKey());
        }
    }

    protected synchronized List<DataSet> getData(N2oTestDataProvider invocation) {
        if (invocation.getFile() == null)
            return new ArrayList<>();
        boolean isInit = getRepositoryData(invocation.getFile()) == null;
        if (isInit || (!readonly && fileExistsOnDisk(invocation.getFile()))) {
            initRepository(invocation);
        }

        return repository.get(richKey(invocation.getFile()));
    }

    protected List<DataSet> getRepositoryData(String key) {
        return repository.get(richKey(key));
    }

    /**
     * Заполняет хранилище данных из файла
     */
    protected void initRepository(N2oTestDataProvider invocation) {
        try {
            InputStream inputStream = getResourceInputStream(invocation);
            List<DataSet> data = loadJson(inputStream, getPrimaryKeyType(invocation), getPrimaryKey(invocation));
            repository.put(richKey(invocation.getFile()), data);
            if (integer.equals(getPrimaryKeyType(invocation))) {
                long maxId = data
                        .stream()
                        .filter(v -> v.get(getPrimaryKey(invocation)) != null)
                        .mapToLong(v -> (Long) v.get(getPrimaryKey(invocation)))
                        .max().orElse(0);
                sequences.put(richKey(invocation.getFile()), new AtomicLong(maxId));
            }
        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

    protected InputStream getResourceInputStream(N2oTestDataProvider invocation) throws IOException {
        String path = getFullResourcePath(invocation.getFile());

        if (fileExistsOnDisk(invocation.getFile())) {
            path = "file:" + getFullPathOnDisk(invocation.getFile());
        }

        return resourceLoader.getResource(path).getInputStream();
    }

    protected String richKey(String key) {
        if (classpathResourcePath != null) return classpathResourcePath + "/" + key;
        if (pathOnDisk != null) return pathOnDisk + "/" + key;
        return key;
    }

    /**
     * Обновляет содержимое файла на диске
     *
     * @param filename Имя файла
     */
    protected void updateFile(String filename) {
        if (!readonly && fileExistsOnDisk(filename)) {
            try (FileWriter fileWriter = new FileWriter(getFullPathOnDisk(filename))) {
                String mapAsJson = objectMapper.writeValueAsString(getRepositoryData(filename));
                fileWriter.write(mapAsJson);
            } catch (IOException e) {
                throw new N2oException(e);
            }
        }
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
        newElement.put(getPrimaryKey(invocation), generateKey(getPrimaryKeyType(invocation), invocation.getFile()));

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
        if (inParams.get(getPrimaryKey(invocation)) == null)
            throw new N2oException("Id is required for operation \"update\"");

        DataSet element = modifiableData
                .stream()
                .filter(buildPredicate(getPrimaryKeyType(invocation), getPrimaryKey(invocation), inParams))
                .findFirst()
                .orElseThrow(() -> new N2oException("No such element"));

        updateElement(element, inParams.entrySet());
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());

        return element;
    }

    private Object updateMany(N2oTestDataProvider invocation,
                              Map<String, Object> inParams,
                              List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList<>(data);
        if (inParams.get(getPrimaryKeys(invocation)) == null)
            throw new N2oException(getPrimaryKeys(invocation) + " is required for operation \"updateMany\"");
        List<DataSet> elements = modifiableData.stream()
                .filter(buildListPredicate(getPrimaryKeyType(invocation), getPrimaryKey(invocation),
                        getPrimaryKeys(invocation), inParams))
                .collect(Collectors.toList());
        Map<String, Object> fields = new HashMap<>(inParams);
        fields.remove(getPrimaryKeys(invocation));
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
        if (inParams.get(getPrimaryKey(invocation)) == null)
            throw new N2oException("Id is required for operation \"updateField\"");
        if (!inParams.containsKey("key") || !inParams.containsKey("value"))
            throw new N2oException("Should contains parameters \"key\", \"value\" for operation \"updateField\"");

        DataSet element = modifiableData
                .stream()
                .filter(buildPredicate(getPrimaryKeyType(invocation), getPrimaryKey(invocation), inParams))
                .findFirst()
                .orElseThrow(() -> new N2oException("No such element"));

        Map<String, Object> fieldData = new HashMap<>();
        fieldData.put(getPrimaryKey(invocation), inParams.get(getPrimaryKey(invocation)));
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
        if (inParams.get(getPrimaryKey(invocation)) == null)
            throw new N2oException("Id is required for operation \"delete\"");

        modifiableData.removeIf(buildPredicate(getPrimaryKeyType(invocation), getPrimaryKey(invocation), inParams));
        updateRepository(invocation.getFile(), modifiableData);
        updateFile(invocation.getFile());

        return null;
    }

    private Object deleteMany(N2oTestDataProvider invocation,
                              Map<String, Object> inParams,
                              List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList(data);
        if (inParams.get(getPrimaryKeys(invocation)) == null)
            throw new N2oException(getPrimaryKeys(invocation) + " is required for operation \"deleteMany\"");
        modifiableData.removeIf(buildListPredicate(getPrimaryKeyType(invocation), getPrimaryKey(invocation),
                getPrimaryKeys(invocation), inParams));
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

    private void updateElement(Map element, Set<Map.Entry<String, Object>> fields) {
        for (Map.Entry field : fields)
            if (field.getValue() instanceof Date)
                element.put(field.getKey(), new SimpleDateFormat("dd.MM.yyy HH:mm:ss").format(field.getValue()));
            else
                element.put(field.getKey(), field.getValue());
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
            String[] splittedFilter = filter.replaceAll("[\\s]", "").split(":");
            String field = splittedFilter[0];
            Object pattern = inParams.get(splittedFilter[2]);

            switch (splittedFilter[1]) {
                case "eq":
                    data = eqFilterData(field, pattern, data);
                    break;
                case "notEq":
                    data = notEqFilterData(field, pattern, data);
                    break;
                case "like":
                    data = likeFilterData(field, pattern, data);
                    break;
                case "in":
                    data = inFilterData(field, pattern, data);
                    break;
                case "notIn":
                    data = notInFilterData(field, pattern, data);
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
                case "contains":
                    data = containsFilterData(field, pattern, data);
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

    private List<DataSet> notEqFilterData(String field, Object pattern, List<DataSet> data) {
        if (pattern != null) {
            data = data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(field) || m.get(field) == null)
                            return false;
                        if (m.get(field) instanceof Number && pattern instanceof Number)
                            return !((Long) ((Number) m.get(field)).longValue()).equals(((Number) pattern).longValue());
                        return !m.get(field).toString().equals(pattern.toString());
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
            String[] splittedField = field.split("\\.");
            String parent = splittedField[0];
            String child = splittedField.length > 1 ? splittedField[1] : null;
            data = data
                    .stream()
                    .filter(m -> {
                        if (child != null) {
                            if (!m.containsKey(parent))
                                return false;
                            if (m.get(parent) instanceof NestedList) {
                                return ((NestedList)m.get(parent)).stream().anyMatch(c ->
                                    ((DataSet) c).containsKey(child) && patterns.contains(((DataSet) c).get(child))
                                );
                            } else {
                                return m.containsKey(field) && patterns.contains(m.get(field));
                            }
                        }
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

    private List<DataSet> notInFilterData(String field, Object pattern, List<DataSet> data) {
        List patterns = pattern instanceof List ? (List) pattern : Arrays.asList(pattern);
        if (patterns != null) {
            String[] splittedField = field.split("\\.");
            String parent = splittedField[0];
            String child = splittedField.length > 1 ? splittedField[1] : null;
            data = data
                    .stream()
                    .filter(m -> {
                        if (child != null) {
                            if (!m.containsKey(parent))
                                return false;
                            if (m.get(parent) instanceof NestedList) {
                                return ((NestedList)m.get(parent)).stream().anyMatch(c ->
                                        ((DataSet) c).containsKey(child) && !patterns.contains(((DataSet) c).get(child))
                                );
                            } else {
                                return m.containsKey(field) && !patterns.contains(m.get(field));
                            }
                        }
                        if (m.get(field) instanceof Number) {
                            List<Long> longPatterns = new ArrayList<>();
                            patterns.forEach(p -> longPatterns.add(((Number) p).longValue()));
                            return !longPatterns.contains(((Number) m.get(field)).longValue());
                        }
                        return !patterns.contains(m.get(field).toString());
                    })
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataSet> containsFilterData(String field, Object pattern, List<DataSet> data) {
        List<?> patterns = pattern instanceof List ? (List<?>) pattern : Collections.singletonList(pattern);
        if (patterns.isEmpty())
            return data;
        List<String> splittedField = new ArrayList<>(Arrays.asList(field.split("\\.")));
        if (splittedField.size() == 1) {
            return data
                    .stream()
                    .filter(m -> {
                        if (!m.containsKey(splittedField.get(0)) || !(m.get(splittedField.get(0)) instanceof List))
                            return false;
                        return m.getList(splittedField.get(0)).containsAll(patterns);
                    })
                    .collect(Collectors.toList());
        }
        String indexedField = splittedField.remove(splittedField.size() - 1);
        String parentField = String.join(".", splittedField);
        return data
                .stream()
                .filter(m -> {
                    if (!m.containsKey(parentField) || !(m.get(parentField) instanceof List))
                        return false;
                    List<Object> mappedData = m.getList(parentField)
                            .stream()
                            .filter(DataSet.class::isInstance)
                            .map(d -> ((DataSet) d).get(indexedField))
                            .collect(Collectors.toList());
                    return mappedData.containsAll(patterns);
                })
                .collect(Collectors.toList());
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
                            LocalDate date = parseToLocalDate(m.get(field).toString());
                            return date.isEqual((ChronoLocalDate) pattern) || date.isBefore((ChronoLocalDate) pattern);
                        }
                        if (pattern instanceof LocalDateTime) {
                            LocalDateTime dateTime = parseToLocalDateTime(m.get(field).toString());
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
                            LocalDate date = parseToLocalDate(m.get(field).toString());
                            return date.isEqual((ChronoLocalDate) pattern) || date.isAfter((ChronoLocalDate) pattern);
                        }
                        if (pattern instanceof LocalDateTime) {
                            LocalDateTime dateTime = parseToLocalDateTime(m.get(field).toString());
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

    private void updateRepository(String key, List<DataSet> newData) {
        repository.put(richKey(key), newData);
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

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
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

    public boolean isReadonly() {
        return readonly;
    }

    public Map<String, List<DataSet>> getRepository() {
        return repository;
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
     * Переводит строковое представление даты в LocalDate
     *
     * @param strDate Строковое представление даты в формате ISO_LOCAL_DATE
     * @return Переменную типа LocalDate, соответствующую строковому представлению даты, при неверном формате
     * строкового представления выбрасывается N2oException
     */
    private LocalDate parseToLocalDate(String strDate) {
        try {
            return LocalDate.parse(strDate);
        } catch (DateTimeParseException e) {
            throw new N2oException("Формат даты, используемый в json, не соответствует ISO_LOCAL_DATE", e);
        }
    }

    /**
     * Переводит строковое представление даты и времени в LocalDateTime.
     * Если строка содержит информацию о часовом поясе, то она будет убрана.
     *
     * @param strDateTime Строковое представление даты и времени в формате ISO_LOCAL_DATE_TIME
     * @return Переменную типа LocalDateTime, соответствующую строковому представлению даты и времени, при неверном формате
     * строкового представления выбрасывается N2oException
     */
    private LocalDateTime parseToLocalDateTime(String strDateTime) {
        try {
            ParsePosition pos = new ParsePosition(0);
            TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_ZONED_DATE_TIME.parseUnresolved(strDateTime, pos);
            if (temporalAccessor == null || pos.getErrorIndex() >= 0 || pos.getIndex() < strDateTime.length())
                return LocalDateTime.parse(strDateTime);
            return ZonedDateTime.parse(strDateTime).toLocalDateTime();
        } catch (DateTimeParseException e) {
            throw new N2oException("Формат даты и времени, используемый в json, не соответствует ISO_LOCAL_DATE_TIME", e);
        }
    }
    private String getPrimaryKey(N2oTestDataProvider invocation) {
        return invocation.getPrimaryKey()!= null ? invocation.getPrimaryKey() : "id";
    }

    private String getPrimaryKeys(N2oTestDataProvider invocation) {
       return getPrimaryKey(invocation) + "s";
    }

    public PrimaryKeyType getPrimaryKeyType(N2oTestDataProvider invocation) {
        return invocation.getPrimaryKeyType()!= null ? invocation.getPrimaryKeyType() : integer;
    }
}
