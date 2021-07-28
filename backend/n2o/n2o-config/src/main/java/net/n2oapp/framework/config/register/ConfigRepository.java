package net.n2oapp.framework.config.register;

import java.util.Map;

/**
 * Репозиторий метаданных N2O
 *
 * @param <K> ключ
 * @param <V> значение
 */
public interface ConfigRepository<K, V> {

    /**
     * Сохранение метаданных в репозиторий
     *
     * @param key   Ключ метаданных
     * @param value Метаданные
     * @return Сохраненная метаданная
     */
    V save(K key, V value);

    /**
     * Получение всех метаданных из репозитория
     *
     * @return
     */
    Map<K, V> getAll();

    /**
     * Удаление метаданных в репозитории
     */
    void clearAll();

}
