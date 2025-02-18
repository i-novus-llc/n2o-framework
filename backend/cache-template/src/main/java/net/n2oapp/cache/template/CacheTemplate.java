package net.n2oapp.cache.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Шаблон сервиса кэширования
 */
@Slf4j
@Getter
@NoArgsConstructor
public class CacheTemplate<K, V> {

    private CacheManager cacheManager;

    public CacheTemplate(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Получение значения из области кэша по его ключу
     *
     * @param cacheRegion Область кэша
     * @param key         Ключ
     * @param callback    Обработчик событий при отсутствии значения в кэше
     * @return Значение из кэша
     */
    @SuppressWarnings("unchecked")
    public V execute(String cacheRegion, K key, CacheCallback<V> callback) {
        Cache cache = cacheManager.getCache(cacheRegion);
        if (cache != null) {
            Cache.ValueWrapper cacheValueWrapper = cache.get(key);
            if (cacheValueWrapper != null)
                return (V) cacheValueWrapper.get();

            return handleCache(cache, key, callback);
        } else {
            log.warn("Не найдена область кэша с именем [{}] для CacheTemplate", cacheRegion);
            return callback.doInCacheMiss();
        }
    }

    /**
     * Заполнить кэш отсутствующим значением
     *
     * @param cache    Кэш
     * @param key      Ключ
     * @param callback Обработчик событий при отсутствии значения в кэше
     * @return Значение из кэша
     */
    protected V handleCache(Cache cache, K key, CacheCallback<V> callback) {
        V value = callback.doInCacheMiss();
        cache.put(key, value);
        return value;
    }
}
