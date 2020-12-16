package net.n2oapp.framework.config.register.route;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Репозиторий
 * @param <K> ключ
 * @param <V> значение
 */
public interface RouteRepository<K, V> {

    V get(K key);

    V put(K key, V value);

    V find (BiPredicate<? super K, ? super V> filter);

    void clear(Predicate<? super K> filter);

}
