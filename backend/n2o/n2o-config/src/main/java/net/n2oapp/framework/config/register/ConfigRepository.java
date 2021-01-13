package net.n2oapp.framework.config.register;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Репозиторий метаданных N2O
 * @param <K> ключ
 * @param <V> значение
 */
public interface ConfigRepository<K, V> {

    /**
     * Сохранение метаданных в репозиторий
     * @param key ключ метаданных
     * @param value метаданные
     * @return - сохраненная методанная
     */
    V save(K key, V value);

    /**
     * Получение всех метаданных из репозитория
     * @return
     */
    Map<K, V> getAll();

    /**
     * Удаление методанных в репозитории
     * @param filter - условие удаления
     */
    void clear(Predicate<? super K> filter);

}
