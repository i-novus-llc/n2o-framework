package net.n2oapp.framework.context.test;

import org.springframework.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author rgalina
 * @since 18.04.2016
 */
public class MockCache implements Cache {

    public static int put = 0;
    public static int hit = 0;
    public static int miss = 0;

    public static void clearStatistics() {
        put = 0;
        hit = 0;
        miss = 0;
    }


    private Map<Object, Object> cacheMap = new HashMap<>();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return cacheMap;
    }

    @Override
    public ValueWrapper get(Object key) {
        if (cacheMap.containsKey(key)) {
            hit++;
            return () -> cacheMap.get(key);
        }
        miss++;
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        if (cacheMap.containsKey(key)) {
            hit++;
            return (T) cacheMap.get(key);
        }
        miss++;
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        ValueWrapper valueWrapper = get(key);
        if (valueWrapper != null)
            return (T) valueWrapper.get();
        try {
            return callable.call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        cacheMap.put(key, value);
        put++;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void evict(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
