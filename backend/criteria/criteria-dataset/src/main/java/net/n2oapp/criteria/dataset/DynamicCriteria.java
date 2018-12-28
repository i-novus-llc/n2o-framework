package net.n2oapp.criteria.dataset;

import net.n2oapp.criteria.api.Criteria;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: operehod
 * Date: 11.11.2014
 * Time: 16:31
 */
public class DynamicCriteria extends Criteria implements Map<String, Object> {


    private Map<String, Object> map = new HashMap<>();


    public DynamicCriteria() {

    }


    public DynamicCriteria(Criteria criteria) {
        setPage(criteria.getPage());
        setCount(criteria.getCount());
        setSortings(criteria.getSortings());
        setSize(criteria.getSize());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
