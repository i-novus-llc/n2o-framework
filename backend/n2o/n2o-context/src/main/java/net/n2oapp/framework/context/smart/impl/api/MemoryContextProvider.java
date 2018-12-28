package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toCollection;

/**
 * @author operehod
 * @since 02.12.2015
 */
public class MemoryContextProvider implements PersistentContextProvider {


    private Set<String> params = new HashSet<>();
    private Set<String> dependentParams = new HashSet<>();
    private ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();

    public MemoryContextProvider(Set<String> params, Set<String> dependentParams) {
        this.params = params;
        this.dependentParams = dependentParams;
    }

    @Override
    public void set(Context ctx, Map<String, Object> values) {
        for (String param : params) {
            Object key = key(ctx, dependentParams, param);
            map.put(key, values.get(param));
        }
    }


    @Override
    public Map<String, Object> get(Context ctx) {
        Map<String, Object> res = new HashMap<>();
        for (String param : params) {
            Object key = key(ctx, dependentParams, param);
            res.put(param, map.get(key));
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private static Object key(Context ctx, Set<String> dependentParams, String param) {
        List res = dependentParams
                .stream()
                .map(ctx::get)
                .collect(toCollection(ArrayList::new));
        res.add(param);
        return res;
    }

    @Override
    public Set<String> getParams() {
        return params;
    }

    @Override
    public Set<String> getDependsOnParams() {
        return dependentParams;
    }
}
