package net.n2oapp.framework.api.test;

import net.n2oapp.framework.api.context.ContextEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author iryabov
 * @since 27.10.2016
 */
public class TestContextEngine extends HashMap<String, Object> implements ContextEngine {

    public TestContextEngine() {
        super();
    }

    public TestContextEngine(Map<String, Object> baseParams) {
        super(baseParams);
    }

    @Override
    public Object get(String param, Map<String, Object> baseParams) {
        Object value = baseParams.get(param);
        return value != null ? value : get(param);
    }

    @Override
    public void set(Map<String, Object> dataSet, Map<String, Object> baseParams) {
        putAll(dataSet);
        putAll(baseParams);
    }

    @Override
    public Object get(String name) {
        return super.get(name);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        putAll(dataSet);
    }
}
