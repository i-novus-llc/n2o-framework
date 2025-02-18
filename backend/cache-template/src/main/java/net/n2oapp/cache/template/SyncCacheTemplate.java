package net.n2oapp.cache.template;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Шаблон сервиса кэширования с синхронизацией получения данных
 */
public class SyncCacheTemplate<V> extends CacheTemplate<String, V> {

    public SyncCacheTemplate(CacheManager cacheManager) {
        super(cacheManager);
    }

    /**
     * Заполнить кэш отсутствующим значением с синхронизацией по ключу
     *
     * @param cache    Кэш
     * @param key      Ключ
     * @param callback Обработчик событий при отсутствии значения в кэше
     * @return Значение из кэша
     */
    @Override
    @SuppressWarnings("unchecked")
    protected V handleCache(Cache cache, String key, CacheCallback<V> callback) {
        synchronized (key.intern()) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null)
                return (V) valueWrapper.get();
            return super.handleCache(cache, key, callback);
        }
    }

}
