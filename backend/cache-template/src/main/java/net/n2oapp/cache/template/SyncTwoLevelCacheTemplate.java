package net.n2oapp.cache.template;

import org.springframework.cache.Cache;

/**
 * Шаблон сервиса двухуровневого кэширования с синхронизацией получения данных по ключу в случае первого и второго промахов
 */
public class SyncTwoLevelCacheTemplate<F, S> extends TwoLevelCacheTemplate<String, F, S> {

    @SuppressWarnings("unchecked")
    @Override
    protected F handleFirstCache(String key, TwoLevelCacheCallback<F, S> callback, Cache firstLevelCache, S secondLevelCacheValue) {
        synchronized (key.intern()) {
            //еще раз читаем, т.к. в кэш могли положить пока ждали
            Cache.ValueWrapper valueWrapper = firstLevelCache.get(key);
            if (valueWrapper != null) {
                F value = (F) valueWrapper.get();
                callback.doInFirstLevelCacheHit(value);
                return value;
            }
            return super.handleFirstCache(key, callback, firstLevelCache, secondLevelCacheValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected S handleSecondCache(String key, TwoLevelCacheCallback<F, S> callback, Cache secondLevelCache) {
        synchronized (key.intern()) {
            //еще раз читаем, т.к. в кэш могли положить пока ждали
            Cache.ValueWrapper valueWrapper = secondLevelCache.get(key);
            if (valueWrapper != null) {
                S value = (S) valueWrapper.get();
                callback.doInSecondLevelCacheHit(value);
                return value;
            }
            return super.handleSecondCache(key, callback, secondLevelCache);
        }
    }
}
