package net.n2oapp.criteria.dataset;

import java.util.*;

import static java.util.Objects.nonNull;

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

    public DataSet(Map<? extends String, ?> m) {
        super();
        m.forEach((k, v) -> put(k, v instanceof DataSet ? new DataSet((DataSet) v) : v instanceof DataList ? new DataList(((DataList) v)) : v));
    }

    public String getId() {
        return get("id") != null ? get("id").toString() : null;
    }

    public void merge(DataSet dataSet) {
        merge(this, dataSet, ALWAYS_EXTEND_VALUE);
    }

    public void merge(DataSet dataSet, ValuePickUpStrategy strategy) {
        merge(this, dataSet, strategy);
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Integer)
            return (Integer) value;
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
        if (value instanceof Long)
            return (Long) value;
        return Long.parseLong((String) value);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Boolean)
            return (Boolean) value;
        return Boolean.parseBoolean((String) value);
    }

    public DataSet getDataSet(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof DataSet)
            return (DataSet) value;
        return new DataSet((Map<String, Object>) value);
    }

    public List<?> getList(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof List)
            return (List<?>) value;
        if (value.getClass().isArray())
            return new ArrayList<>(Arrays.asList((Object[]) value));
        return new ArrayList<>((Collection<?>) value);
    }


    private static void merge(NestedMap main, NestedMap extend, ValuePickUpStrategy strategy) {
        if (main == extend) return;
        if (extend == null) return;
        for (String fieldName : extend.keySet()) {
            Object mainValue = main.get(fieldName);
            Object extendValue = extend.get(fieldName);
            if (mainValue != null && (mainValue instanceof NestedMap && extendValue instanceof NestedMap)) {
                merge((NestedMap) mainValue, (NestedMap) extendValue, strategy);
            } else if (mainValue != null && mainValue instanceof List && extendValue instanceof List) {
                mergeArrays((List) mainValue, (List) extendValue);
            } else if (nonNull(strategy.pickUp(mainValue, extendValue))) {
                main.put(fieldName, strategy.pickUp(mainValue, extendValue));
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
            if (entry.getValue() instanceof DataSet) {
                Set<String> childrenKeySet = ((DataSet) entry.getValue()).flatKeySet();
                for (String childKey : childrenKeySet) {
                    result.add(entry.getKey() + "." + childKey);
                }
            } else {
                result.add(entry.getKey());
            }
        }
        return result;
    }


    private static void mergeArrays(List mainArray, List updateArray) {
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
