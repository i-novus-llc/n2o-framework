package net.n2oapp.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author operehod
 * @since 13.05.2015
 */
public class CacheTemplateByMap {

    private volatile Map<List, Object> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(Supplier<T> supplier, Object... keys) {
        List key = Arrays.asList(keys);
        if (!cache.containsKey(key)) {
            addValueToCache(key, supplier.get());
        }
        return (T) cache.get(key);
    }

    private synchronized <T> void addValueToCache(List key, T t) {
        if (!cache.containsKey(key)) {
            if (t != null)
                cache.put(key, t);
        }
    }


}
