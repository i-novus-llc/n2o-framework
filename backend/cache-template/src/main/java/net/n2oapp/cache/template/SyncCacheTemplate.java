package net.n2oapp.cache.template;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Шаблон сервиса кэширования с синхронизацией получения данных по ключу в случае промаха
 */
public class SyncCacheTemplate<E> extends CacheTemplate<String, E> {

    public SyncCacheTemplate() {
    }

    public SyncCacheTemplate(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected E handleCache(String key, CacheCallback<E> callback, Cache cache) {
        synchronized (key.intern()) {
            //еще раз читаем, т.к. в кэш могли положить пока ждали
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                E value = (E)valueWrapper.get();
                callback.doInCacheHit(value);
                return value;
            }
            return super.handleCache(key, callback, cache);
        }
    }

}
