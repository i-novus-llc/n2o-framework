package net.n2oapp.cache.template;

import org.springframework.cache.support.NoOpCache;

public class NoOpCacheTemplate<K, T> extends CacheTemplate<K,T> {

    @Override
    public T execute(String cacheRegion, K key, CacheCallback<T> callback) {
        return handleCache(key, callback, new NoOpCache(cacheRegion));
    }
}
