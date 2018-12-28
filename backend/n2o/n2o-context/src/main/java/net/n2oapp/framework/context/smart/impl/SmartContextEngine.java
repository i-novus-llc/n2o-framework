package net.n2oapp.framework.context.smart.impl;

import net.n2oapp.framework.context.smart.impl.cache.ContextCacheTemplateImpl;

import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * Реализация работы контекстных значений, основанная на хранении данных в кэше
 */
public class SmartContextEngine extends AbstractContextEngine {

    public SmartContextEngine(ContextCacheTemplateImpl cacheTemplate) {
        super(cacheTemplate);
    }

    @Override
    public Object get(String param) {
        return get(param, emptyMap());
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        set(dataSet, emptyMap());
    }

    @Override
    public void set(String key, Object value) {
        set(Collections.singletonMap(key, value));
    }

    @Override
    public void refresh() {
        cacheTemplate.evictCache();
    }
}
