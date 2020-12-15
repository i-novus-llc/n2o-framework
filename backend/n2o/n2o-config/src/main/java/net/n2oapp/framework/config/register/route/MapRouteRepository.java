package net.n2oapp.framework.config.register.route;


import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Predicate;

public class MapRouteRepository<K extends Comparable, V> implements RouteRepository<K, V> {

    private final SortedMap<K, V> register = new ConcurrentSkipListMap<>();

    @Override
    public V get(K key) {
        return register.get(key);
    }

    @Override
    public V put(K key, V value) {
        return register.put(key, value);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return register.entrySet().iterator();
    }

    @Override
    public void clear(Predicate<? super K> filter) {
        register.keySet().removeIf(filter);
    }
}
