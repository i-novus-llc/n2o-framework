package net.n2oapp.cache.template;

import org.springframework.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MockCache implements Cache {

    private AtomicInteger put = new AtomicInteger(0);
    private AtomicInteger hit = new AtomicInteger(0);
    private AtomicInteger miss = new AtomicInteger(0);

    public void clearStatistics() {
        put.set(0);
        hit.set(0);
        miss.set(0);
    }


    private Map<Object, Object> cacheMap = new ConcurrentHashMap<>();

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
            hit.incrementAndGet();
            return () -> cacheMap.get(key);
        }
        miss.incrementAndGet();
        return null;
    }



    @Override
    public <T> T get(Object key, Class<T> type) {
        if (cacheMap.containsKey(key)) {
            hit.incrementAndGet();
            return (T) cacheMap.get(key);
        }
        miss.incrementAndGet();
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper valueWrapper = get(key);
        if (valueWrapper == null)
            try {
                return valueLoader.call();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        return (T) valueWrapper.get();
    }

    @Override
    public void put(Object key, Object value) {
        cacheMap.put(key, value);
        put.incrementAndGet();
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

    public int getHit() {
        return hit.get();
    }

    public int getMiss() {
        return miss.get();
    }

    public int getPut() {
        return put.get();
    }
}
