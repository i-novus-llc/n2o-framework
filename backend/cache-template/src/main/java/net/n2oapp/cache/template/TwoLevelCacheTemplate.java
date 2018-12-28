package net.n2oapp.cache.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Шаблон сервиса двухуровневого кэширования
 */
public class TwoLevelCacheTemplate<K, F, S> {
    private static final Logger log = LoggerFactory.getLogger(TwoLevelCacheTemplate.class);

    private CacheManager cacheManager;

    public TwoLevelCacheTemplate() {
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private CacheManager getCacheManager() {
        return cacheManager;
    }

    @SuppressWarnings("unchecked")
    public F execute(String firstLevelRegion, String secondLevelRegion, K key,
                     TwoLevelCacheCallback<F, S> callback) {
        Cache firstLevelCache = null;
        if (firstLevelRegion != null) {
            firstLevelCache = getCacheManager().getCache(firstLevelRegion);
            if (firstLevelCache != null) {
                Cache.ValueWrapper firstLevelCacheValueWrapper = firstLevelCache.get(key);
                if (firstLevelCacheValueWrapper != null) {
                    F f = (F) firstLevelCacheValueWrapper.get();
                    callback.doInFirstLevelCacheHit(f);
                    return f;
                }
            } else {
                log.warn("Cannot find cache named [" + firstLevelRegion + "] for TwoLevelCacheTemplate");
            }
        }
        Cache secondLevelCache = null;
        if (secondLevelRegion != null) {
            secondLevelCache = getCacheManager().getCache(secondLevelRegion);
            if (secondLevelCache != null) {
                Cache.ValueWrapper secondLevelCacheValueWrapper = secondLevelCache.get(key);
                if (secondLevelCacheValueWrapper != null) {
                    S secondLevelCacheValue = (S) secondLevelCacheValueWrapper.get();
                    if (secondLevelCacheValue != null) {
                        callback.doInSecondLevelCacheHit(secondLevelCacheValue);
                        return handleFirstCache(key, callback, firstLevelCache, secondLevelCacheValue);
                    }
                }
            } else {
                log.warn(
                        "Cannot find cache named [" + secondLevelRegion + "] for TwoLevelCacheTemplate");
            }
        }
        S secondLevelCacheValue = handleSecondCache(key, callback, secondLevelCache);
        return handleFirstCache(key, callback, firstLevelCache, secondLevelCacheValue);
    }

    protected F handleFirstCache(K key, TwoLevelCacheCallback<F, S> callback, Cache firstLevelCache, S secondLevelCacheValue) {
        F firstLevelCacheValue = callback.doInFirstLevelCacheMiss(secondLevelCacheValue);
        if (firstLevelCache != null) {
            firstLevelCache.put(key, firstLevelCacheValue);
        }
        return firstLevelCacheValue;
    }

    protected S handleSecondCache(K key, TwoLevelCacheCallback<F, S> callback, Cache secondLevelCache) {
        S secondLevelCacheValue = callback.doInSecondLevelCacheMiss();
        if (secondLevelCache != null) {
            secondLevelCache.put(key, secondLevelCacheValue);
        }
        return secondLevelCacheValue;
    }
}
