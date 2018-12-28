package net.n2oapp.framework.context.test;

import net.n2oapp.framework.context.smart.impl.cache.ContextCacheTemplateImpl;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author rgalina
 * @since 18.04.2016
 */
public class MockCacheManager implements CacheManager {

    private MockCache mockCache;

    public MockCacheManager(MockCache mockCache) {
        this.mockCache = mockCache;
    }

    @Override
    public Cache getCache(String name) {
        return mockCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Arrays.asList(ContextCacheTemplateImpl.CACHE_NAME);
    }
}
