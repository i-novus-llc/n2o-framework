package net.n2oapp.framework.config.register.route;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Репозиторий
 * @param <K> ключ
 * @param <V> значение
 */
public interface RouteRepository<K, V> {

    V get(K key);

    V put(K key, V value);

    Iterator<Map.Entry<K, V>> iterator();

    void clear(Predicate<? super K> filter);

}
