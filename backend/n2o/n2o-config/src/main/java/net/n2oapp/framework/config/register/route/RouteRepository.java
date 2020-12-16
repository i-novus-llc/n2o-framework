package net.n2oapp.framework.config.register.route;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Репозиторий
 * @param <K> ключ
 * @param <V> значение
 */
public interface RouteRepository<K, V> {

    V put(K key, V value);

    Map<K, V> getAll();

    void clear(Predicate<? super K> filter);

}
