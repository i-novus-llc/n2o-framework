package net.n2oapp.cache.template;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = { "spring.cache.jcache.config=classpath:ehcache.xml" })
@EnableCaching
@AutoConfigureCache(cacheProvider = CacheType.JCACHE)
class TwoLevelCacheTemplateTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testFirstCacheMissAndSecondCacheMiss() {
        TwoLevelCacheTemplate<Integer, String, String> twoLevelCacheTemplate = new TwoLevelCacheTemplate<>();
        twoLevelCacheTemplate.setCacheManager(cacheManager);
        String value = twoLevelCacheTemplate.execute("first", "second", 1, new TwoLevelCacheCallback<String, String>() {
            @Override
            public String doInFirstLevelCacheMiss(String valueFromSecondLevelCache) {
                return valueFromSecondLevelCache;
            }

            @Override
            public String doInSecondLevelCacheMiss() {
                return "test";
            }

        });
        assertEquals("test", value);
    }

    @Test
    void testFirstCacheMissAndSecondCacheHit() {
        TwoLevelCacheTemplate<Integer, String, String> twoLevelCacheTemplate = new TwoLevelCacheTemplate<>();
        twoLevelCacheTemplate.setCacheManager(cacheManager);
        cacheManager.getCache("second").put(1, "test");
        String value = twoLevelCacheTemplate.execute("first", "second", 1, new TwoLevelCacheCallback<String, String>() {
            @Override
            public String doInFirstLevelCacheMiss(String valueFromSecondLevelCache) {
                return valueFromSecondLevelCache;
            }

            @Override
            public String doInSecondLevelCacheMiss() {
                throw new AssertionError("second level cache could not be invoked");
            }

        });
        assertEquals("test", value);
    }

    @Test
    void testFirstCacheHit() {
        TwoLevelCacheTemplate<Integer, String, String> twoLevelCacheTemplate = new TwoLevelCacheTemplate<>();
        twoLevelCacheTemplate.setCacheManager(cacheManager);
        cacheManager.getCache("first").put(1, "test");
        String value = twoLevelCacheTemplate.execute("first", "second", 1, new TwoLevelCacheCallback<String, String>() {
            @Override
            public String doInFirstLevelCacheMiss(String valueFromSecondLevelCache) {
                throw new AssertionError("first level cache could not be invoked");
            }

            @Override
            public String doInSecondLevelCacheMiss() {
                throw new AssertionError("second level cache could not be invoked");
            }

        });
        assertEquals("test", value);
    }
}
