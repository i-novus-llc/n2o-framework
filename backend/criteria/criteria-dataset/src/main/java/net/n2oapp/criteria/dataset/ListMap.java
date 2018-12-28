package net.n2oapp.criteria.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author operehod
 * @since 03.06.2015
 */
public class ListMap<K, V> extends HashMap<K, List<V>> {


    public void forEachValue(BiConsumer<K, V> action) {
        for (K k : keySet()) {
            for (V v : getList(k)) {
                action.accept(k, v);
            }
        }
    }

    public void putToList(K key, V value) {
        getList(key).add(value);
    }

    public void putIfNotExists(K key, V value) {
        List<V> list = getList(key);
        if (!list.contains(value))
            list.add(value);
    }

    private List<V> getList(K key) {
        List<V> list = get(key);
        if (list == null) {
            list = new ArrayList<>();
            put(key, list);
        }
        return list;
    }


}
