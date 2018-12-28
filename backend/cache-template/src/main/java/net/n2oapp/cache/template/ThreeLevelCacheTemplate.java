package net.n2oapp.cache.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Шаблон сервиса трехуровнего кэширования
 */
public class ThreeLevelCacheTemplate<F, S, T> {
    protected static final Logger log = LoggerFactory.getLogger(ThreeLevelCacheTemplate.class);

    private CacheManager cacheManager;

    public ThreeLevelCacheTemplate() {
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    @SuppressWarnings("unchecked")
    public F execute(String firstLevelRegion, String secondLevelRegion, String thirdLevelRegion, Object firstKey,
                     Object secondKey, Object thirdKey,
                     ThreeLevelCacheCallback<F, S, T> callback) {
        Cache firstLevelCache = null;
        if (firstLevelRegion != null) {
            firstLevelCache = getCacheManager().getCache(firstLevelRegion);
            if (firstLevelCache != null) {
                Cache.ValueWrapper firstLevelCacheValueWrapper = firstLevelCache.get(firstKey);
                if (firstLevelCacheValueWrapper != null) {
                    F f = (F) firstLevelCacheValueWrapper.get();
                    callback.doInFirstLevelCacheHit(f);
                    return f;
                }
            } else {
                log.warn(
                        "Cannot find cache named [" + firstLevelRegion + "] for ThreeLevelCacheTemplate");
            }
        }
        Cache secondLevelCache = null;
        if (secondLevelRegion != null) {
            secondLevelCache = getCacheManager().getCache(secondLevelRegion);
            if (secondLevelCache != null) {
                Cache.ValueWrapper secondLevelCacheValueWrapper = secondLevelCache.get(secondKey);
                if (secondLevelCacheValueWrapper != null) {
                    S secondLevelCacheValue = (S) secondLevelCacheValueWrapper.get();
                    if (secondLevelCacheValue != null) {
                        callback.doInSecondLevelCacheHit(secondLevelCacheValue);
                        return handleFirstCache(firstKey, secondLevelCacheValue, firstLevelCache, callback);
                    }
                }
            } else {
                log.warn(
                        "Cannot find cache named [" + secondLevelRegion + "] for ThreeLevelCacheTemplate");
            }
        }
        Cache thirdLevelCache = null;
        if (thirdLevelRegion != null) {
            thirdLevelCache = getCacheManager().getCache(thirdLevelRegion);
            if (thirdLevelCache != null) {
                Cache.ValueWrapper thirdLevelCacheValueWrapper = thirdLevelCache.get(thirdKey);
                if (thirdLevelCacheValueWrapper != null) {
                    T thirdLevelCacheValue = (T) thirdLevelCacheValueWrapper.get();
                    if (thirdLevelCacheValue != null) {
                        callback.doInThirdLevelCacheHit(thirdLevelCacheValue);
                        F result = handleSecondCache(firstKey, secondKey, callback, firstLevelCache, secondLevelCache, thirdLevelCacheValue);
                        if (result != null) return result;
                    }
                }
            } else {
                log.warn(
                        "Cannot find cache named [" + thirdLevelRegion + "] for ThreeLevelCacheTemplate");
            }
        }

        return handleThirdCache(firstKey, secondKey, thirdKey, callback, firstLevelCache, secondLevelCache, thirdLevelCache);
    }

    protected F handleThirdCache(Object firstKey, Object secondKey, Object thirdKey, ThreeLevelCacheCallback<F, S, T> callback, Cache firstLevelCache, Cache secondLevelCache, Cache thirdLevelCache) {
        T thirdLevelCacheValue = callback.doInThirdLevelCacheMiss();
        if (thirdLevelCacheValue != null) {
            thirdLevelCache.put(thirdKey, thirdLevelCacheValue);
            F result = handleSecondCache(firstKey, secondKey, callback, firstLevelCache, secondLevelCache, thirdLevelCacheValue);
            if (result != null) return result;
        }
        return null;
    }

    protected F handleSecondCache(Object firstKey, Object secondKey, ThreeLevelCacheCallback<F, S, T> callback, Cache firstLevelCache, Cache secondLevelCache, T thirdLevelCacheValue) {
        S secondLevelCacheValue = callback.doInSecondLevelCacheMiss(thirdLevelCacheValue);
        if (secondLevelCacheValue != null) {
            secondLevelCache.put(secondKey, secondLevelCacheValue);
            return handleFirstCache(firstKey, secondLevelCacheValue, firstLevelCache, callback);
        }
        return null;
    }


    protected F handleFirstCache(Object firstKey, S secondLevelCacheValue, Cache firstLevelCache,
                               ThreeLevelCacheCallback<F, S, T> callback) {
        F firstLevelCacheValue = callback.doInFirstLevelCacheMiss(secondLevelCacheValue);
        if (firstLevelCache != null) {
            firstLevelCache.put(firstKey, firstLevelCacheValue);
        }
        return firstLevelCacheValue;
    }
}
