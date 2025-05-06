package net.n2oapp.criteria.dataset;

import java.util.*;

import static java.util.Objects.nonNull;
import static net.n2oapp.criteria.dataset.ArrayMergeStrategyEnum.MERGE;
import static net.n2oapp.criteria.dataset.ArrayMergeStrategyEnum.REPLACE;

/**
 * Данные объекта
 */
public class DataSet extends NestedMap {
    public DataSet() {
    }

    public DataSet(String key, Object value) {
        super();
        put(key, value);
    }

    public DataSet add(String key, Object value) {
        put(key, value);
        return this;
    }

    public DataSet(Map<String, ?> m) {
        super();
        m.forEach((k, v) -> {
            if (v instanceof DataSet dataSet) {
                put(k, new DataSet(dataSet));
            } else if (v instanceof DataList dataList) {
                put(k, new DataList(dataList));
            } else {
                put(k, v);
            }
        });
    }

    public String getId() {
        return get("id") != null ? get("id").toString() : null;
    }

    public void merge(DataSet dataSet) {
        merge(this, dataSet, ALWAYS_EXTEND_VALUE, MERGE, false);
    }

    public void merge(DataSet dataSet, Boolean addNullValues) {
        merge(this, dataSet, ALWAYS_EXTEND_VALUE, MERGE, addNullValues);
    }

    public void merge(DataSet dataSet, ArrayMergeStrategyEnum mergeStrategy, Boolean addNullValues) {
        merge(this, dataSet, ALWAYS_EXTEND_VALUE, mergeStrategy, addNullValues);
    }

    public void merge(DataSet dataSet, ValuePickUpStrategy strategy) {
        merge(this, dataSet, strategy, MERGE, false);
    }

    public void merge(DataSet dataSet, ValuePickUpStrategy strategy, Boolean addNullValues) {
        merge(this, dataSet, strategy, MERGE, addNullValues);
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Integer integerValue)
            return integerValue;
        return Integer.parseInt((String) value);
    }

    public String getString(String key) {
        Object value = get(key);
        return (String) value;
    }

    public Long getLong(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Long longValue)
            return longValue;
        return Long.parseLong((String) value);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Boolean booleanValue)
            return booleanValue;
        return Boolean.parseBoolean((String) value);
    }

    public DataSet getDataSet(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof DataSet dataSetValue)
            return dataSetValue;
        return new DataSet((Map<String, Object>) value);
    }

    public List<?> getList(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof List listValue)
            return listValue;
        if (value.getClass().isArray())
            return new ArrayList<>(Arrays.asList((Object[]) value));
        return new ArrayList<>((Collection<?>) value);
    }


    private static void merge(NestedMap main, NestedMap extend, ValuePickUpStrategy strategy,
                              ArrayMergeStrategyEnum mergeStrategy, Boolean addNullValues) {
        if (main == extend) return;
        if (extend == null) return;
        for (Map.Entry<String, Object> entry : extend.entrySet()) {
            String fieldName = entry.getKey();
            Object extendValue = entry.getValue();
            Object mainValue = main.get(fieldName);
            if (mainValue instanceof NestedMap mainVal && extendValue instanceof NestedMap extendVal) {
                merge(mainVal, extendVal, strategy, mergeStrategy, addNullValues);
            } else if (mainValue instanceof List mainVal && extendValue instanceof List extendVal) {
                mergeArrays(mainVal, extendVal, mergeStrategy);
            } else if (nonNull(strategy.pickUp(mainValue, extendValue))) {
                main.put(fieldName, strategy.pickUp(mainValue, extendValue));
            } else if (addNullValues) {
                main.put(fieldName, null);
            }
        }
    }

    @Override
    protected NestedMap createNestedMap(Map map) {
        if (map == null)
            return new DataSet();
        else
            return new DataSet(map);
    }

    @Override
    protected NestedList createNestedList(List list) {
        if (list == null)
            return new DataList();
        else
            return new DataList(list);
    }

    /**
     * Плоский список ключей вложенной мапы
     *
     * @return Список с плоскими ключами
     */
    public Set<String> flatKeySet() {
        Set<String> result = new LinkedHashSet<>();
        for (Map.Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof DataSet dataSet) {
                Set<String> childrenKeySet = dataSet.flatKeySet();
                for (String childKey : childrenKeySet) {
                    result.add(entry.getKey() + "." + childKey);
                }
            } else {
                result.add(entry.getKey());
            }
        }
        return result;
    }


    private static void mergeArrays(List mainArray, List updateArray, ArrayMergeStrategyEnum arrayMergeStrategy) {
        if (REPLACE.equals(arrayMergeStrategy))
            mainArray.clear();
        mainArray.addAll(updateArray);
    }

    public static boolean isSpreadKey(String key) {
        return key.contains("*.");
    }

    public interface ValuePickUpStrategy {
        Object pickUp(Object mainValue, Object extendValue);
    }

    public static final ValuePickUpStrategy ALWAYS_EXTEND_VALUE = (mainValue, extendValue) -> extendValue;
    public static final ValuePickUpStrategy EXTEND_IF_VALUE_NOT_NULL = (mainValue, extendValue) -> extendValue != null ? extendValue : mainValue;


}
