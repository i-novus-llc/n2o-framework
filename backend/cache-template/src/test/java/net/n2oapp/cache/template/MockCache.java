package net.n2oapp.cache.template;

import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Кэш-заглушка для тестирования
 */
@SuppressWarnings("NullableProblems")
class MockCache implements Cache {

    private final AtomicInteger put = new AtomicInteger(0);
    private final AtomicInteger hit = new AtomicInteger(0);
    private final AtomicInteger miss = new AtomicInteger(0);

    private final Map<Object, Object> cacheMap = new ConcurrentHashMap<>();

    public void clearStatistics() {
        put.set(0);
        hit.set(0);
        miss.set(0);
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
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
            //noinspection unchecked
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
        //noinspection unchecked
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
