package net.n2oapp.cache.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Шаблон сервиса кэширования
 */
public class CacheTemplate<K,E> {
    private static final Logger log = LoggerFactory.getLogger(CacheTemplate.class);

    private CacheManager cacheManager;

    public CacheTemplate() {
    }

    public CacheTemplate(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @SuppressWarnings("unchecked")
    public E execute(String cacheRegion, K key, CacheCallback<E> callback) {
        Cache cache = getCacheManager().getCache(cacheRegion);
        if (cache != null) {
            Cache.ValueWrapper cacheValueWrapper = cache.get(key);
            if (cacheValueWrapper != null) {
                E e = (E) cacheValueWrapper.get();
                callback.doInCacheHit(e);
                return e;
            }
        } else {
            log.warn("Cannot find cache named [" + cacheRegion + "] for CacheTemplate");
            return callback.doInCacheMiss();
        }
        return handleCache(key, callback, cache);
    }

    protected E handleCache(K key, CacheCallback<E> callback, Cache cache) {
        E value = callback.doInCacheMiss();
        cache.put(key, value);
        return value;
    }
}
