package net.n2oapp.framework.ui.context;

import net.n2oapp.framework.api.context.ContextEngine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapContextEngine implements ContextEngine {
    private Map<String, Object> context = new ConcurrentHashMap<>();

    @Override
    public Object get(String param, Map<String, Object> baseParams) {
        return baseParams.getOrDefault(param, context.get(param));
    }

    @Override
    public void set(Map<String, Object> dataSet, Map<String, Object> baseParams) {
        context.putAll(dataSet);
    }

    @Override
    public Object get(String name) {
        return context.get(name);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        context.putAll(dataSet);
    }
}
