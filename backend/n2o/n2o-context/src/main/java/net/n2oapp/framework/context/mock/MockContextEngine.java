package net.n2oapp.framework.context.mock;

import net.n2oapp.framework.context.smart.impl.AbstractContextEngine;
import net.n2oapp.framework.context.smart.impl.cache.DummyContextCacheTemplate;

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * User: operehod
 * Date: 02.02.2015
 * Time: 13:01
 */
public class MockContextEngine extends AbstractContextEngine {


    private static class MockContextCacheTemplate extends DummyContextCacheTemplate {
    }


    public MockContextEngine() {
        super(new MockContextCacheTemplate());
    }

    @Override
    public Object get(String name) {
        return get(name, emptyMap());
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        set(dataSet, emptyMap());
    }

}
