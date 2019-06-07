package net.n2oapp.framework.engine.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Тестовый провайдер данных из json файла
 */
@Component
public class TestDataProviderEngine implements MapInvocationEngine<N2oTestDataProvider>, ResourceLoaderAware {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final Map<String, List<DataSet>> repository = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> sequences = new ConcurrentHashMap<>();

    @Override
    public Class<? extends N2oTestDataProvider> getType() {
        return N2oTestDataProvider.class;
    }

    @Override
    public Object invoke(N2oTestDataProvider invocation, Map<String, Object> inParams) {
        return execute(invocation, inParams, getData(invocation.getFile()));
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
            case delete:
                return delete(invocation, inParams, data);
            case count:
                return repository.get(invocation.getFile()).size();
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
        List<DataSet> modifiableData = new ArrayList<>(data);
        modifiableData = filter(filters, inParams, modifiableData);
        return modifiableData.isEmpty() ? null : modifiableData.get(0);
    }

    private Object create(N2oTestDataProvider invocation,
                          Map<String, Object> inParams,
                          List<DataSet> data) {

        List<DataSet> modifiableData = new ArrayList<>(data);
        DataSet newElement = new DataSet();
        newElement.put("id", sequences.get(invocation.getFile()).incrementAndGet());

        updateElement(newElement, inParams.entrySet());

        modifiableData.add(0, newElement);
        updateRepository(invocation.getFile(), modifiableData);

        return newElement;
    }

    private Object update(N2oTestDataProvider invocation,
                          Map<String, Object> inParams,
                          List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList<>(data);
        if (inParams.get("id") == null)
            throw new N2oException("Id is required for operation \"update\"");
        Long id = ((Number) inParams.get("id")).longValue();

        DataSet element = modifiableData
                .stream()
                .filter(v -> id.equals(((Number)v.get("id")).longValue()))
                .findFirst()
                .orElseThrow(() -> new N2oException("No such element"));

        updateElement(element, inParams.entrySet());
        updateRepository(invocation.getFile(), modifiableData);

        return null;
    }

    private Object delete(N2oTestDataProvider invocation,
                          Map<String, Object> inParams,
                          List<DataSet> data) {
        List<DataSet> modifiableData = new ArrayList(data);
        if (inParams.get("id") == null)
            throw new N2oException("Id is required for operation \"delete\"");
        Long id = ((Number) inParams.get("id")).longValue();

        modifiableData.removeIf(elem -> id.equals(((Number)elem.get("id")).longValue()));
        updateRepository(invocation.getFile(), modifiableData);
        return null;
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
        if (filters == null || filters.size() == 0)
            return data;
        for (String filter : filters) {
            if (filter.contains(":eq")) {
                String[] splittedFilter = filter.replaceAll(" ", "").split(":eq");
                Object pattern = inParams.get(splittedFilter[1].replace(":", ""));
                if (pattern != null) {
                    data = data
                            .stream()
                            .filter(m -> {
                                if (!m.containsKey(splittedFilter[0]) || m.get(splittedFilter[0]) == null)
                                    return false;
                                if (m.get(splittedFilter[0]) instanceof Number && pattern instanceof Number)
                                    return ((Long) ((Number) m.get(splittedFilter[0])).longValue()).equals(((Number) pattern).longValue());
                                return m.get(splittedFilter[0]).toString().equals(pattern.toString());
                            })
                            .collect(Collectors.toList());
                }
            } else if (filter.contains(":like")) {
                String[] splittedFilter = filter.replaceAll(" ", "").split(":like");
                String pattern = inParams.get(splittedFilter[1].replace(":", "")).toString();
                if (pattern != null) {
                    data = data
                            .stream()
                            .filter(m -> {
                                if (!m.containsKey(splittedFilter[0]) || m.get(splittedFilter[0]) == null)
                                    return false;
                                return ((m.get(splittedFilter[0]))).toString().toLowerCase()
                                        .contains(pattern.toLowerCase());
                            })
                            .collect(Collectors.toList());
                }
            } else if (filter.contains(":in")) {
                String[] splittedFilter = filter.replaceAll(" ", "").split(":in");
                List patterns = (List) inParams.get(splittedFilter[1].replace(":", ""));
                if (patterns != null) {
                    data = data
                            .stream()
                            .filter(m -> {
                                if (!m.containsKey(splittedFilter[0]) || m.get(splittedFilter[0]) == null)
                                    return false;
                                if (m.get(splittedFilter[0]) instanceof Number) {
                                    List<Long> longPatterns = new ArrayList<>();
                                    patterns.forEach(p -> longPatterns.add(((Number) p).longValue()));
                                    return longPatterns.contains(((Number) m.get(splittedFilter[0])).longValue());
                                }
                                for (Object p : patterns) {
                                    if (p.toString().equals(m.get(splittedFilter[0]).toString()))
                                        return true;
                                }
                                return false;
                            })
                            .collect(Collectors.toList());
                }
            } else if (filter.contains(":more")) {
                String[] splittedFilter = filter.replaceAll(" ", "").split(":more");
                Comparable pattern = (Comparable) inParams.get(splittedFilter[1].replace(":", ""));
                if (pattern != null) {
                    data = data
                            .stream()
                            .filter(m -> {
                                if (!m.containsKey(splittedFilter[0]) || m.get(splittedFilter[0]) == null)
                                    return false;
                                if (m.get(splittedFilter[0]) instanceof Number && pattern instanceof Number) {
                                    return ((Long) ((Number) m.get(splittedFilter[0])).longValue()).compareTo(((Number) pattern).longValue()) > 0;
                                }
                                return ((m.get(splittedFilter[0]).toString())).compareTo(pattern.toString()) > 0;
                            })
                            .collect(Collectors.toList());
                }
            } else if (filter.contains(":less")) {
                String[] splittedFilter = filter.replaceAll(" ", "").split(":less");
                Comparable pattern = (Comparable) inParams.get(splittedFilter[1].replace(":", ""));
                if (pattern != null) {
                    data = data
                            .stream()
                            .filter(m -> {
                                if (!m.containsKey(splittedFilter[0]) || m.get(splittedFilter[0]) == null)
                                    return false;
                                if (m.get(splittedFilter[0]) instanceof Number && pattern instanceof Number) {
                                    return ((Long) ((Number) m.get(splittedFilter[0])).longValue()).compareTo(((Number) pattern).longValue()) < 0;
                                }
                                return ((m.get(splittedFilter[0]).toString())).compareTo(pattern.toString()) < 0;
                            })
                            .collect(Collectors.toList());
                }
            }
        }
        return data;
    }

    private void sort(List<String> sortings, Map<String, Object> inParams, List<DataSet> data) {
        if (sortings == null || sortings.size() == 0)
            return;

        String sorting = sortings.get(0);
        String[] splittedSorting = sorting.replaceAll(" ", "").split(":");
        String direction = (String) inParams.get(splittedSorting[1]);

        if (data.size() < 1)
            return;

        Comparator comparator = Comparator.comparing(m -> (Comparable) (((LinkedHashMap) m).get(splittedSorting[0])),
                Comparator.nullsLast(Comparator.naturalOrder()));

        if (comparator == null)
            return;

        if (direction.equals("desc"))
            comparator = comparator.reversed();

        data.sort(comparator);
    }

    private synchronized List<DataSet> getData(String fileName) {
        if (fileName == null)
            return new ArrayList<>();
        if (!repository.containsKey(fileName)) {
            initRepository(fileName);
        }
        return repository.get(fileName);
    }

    private void updateRepository(String key, List<DataSet> newData) {
        repository.put(key, newData);
    }

    /**
     * Заполняет хранилище данных из файла
     */
    private void initRepository(String fileName) {
        try {
            InputStream inputStream = resourceLoader.getResource("classpath:" + fileName).getInputStream();
            List<DataSet> data = loadJson(inputStream);
            repository.put(fileName, data);
            long maxId = data
                    .stream()
                    .filter(v -> v.get("id") != null)
                    .mapToLong(v -> (Long) v.get("id"))
                    .max().orElse(-1);
            if (maxId != -1)
                sequences.put(fileName, new AtomicLong(maxId));
        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

    private List<DataSet> loadJson(InputStream is) throws IOException {
        List<DataSet> dataList = Arrays.asList(new ObjectMapper().readValue(is, DataSet[].class));
        for (DataSet data : dataList) {
            if (data.containsKey("id")) {
                if (data.get("id") instanceof Number)
                    data.put("id", ((Number) data.get("id")).longValue());
                else if (data.get("id") instanceof String) {
                    data.put("id", Long.valueOf((String) data.get("id")));
                }
            }
        }
        return dataList;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
