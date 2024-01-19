package net.n2oapp.cache.template;

import net.sf.ehcache.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(CacheTestConfig.class)
public class TwoLevelCacheTemplateTest {

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

    @Test
    void testWriteBehindPutSyncEvictSync() {
        Cache cache = cacheManager.getCache("writeBehindCache");
        net.sf.ehcache.Cache nativeCache = (net.sf.ehcache.Cache) cache.getNativeCache();
        System.out.println("cache put:" + "test");
        nativeCache.putWithWriter(new Element(1, "test"));
        System.out.println("cache get:" + cache.get(1).get());

        System.out.println("cache evict:" + 1);
        System.out.println("cache get:" + cache.get(1).get());
        nativeCache.removeWithWriter(1);
        System.out.println("cache get:" + cache.get(1));
    }

    @Test
    void testWriteBehindPutEvictSync() {
        Cache cache = cacheManager.getCache("writeBehindCache");
        net.sf.ehcache.Cache nativeCache = (net.sf.ehcache.Cache) cache.getNativeCache();
        System.out.println("cache put:" + "test");
        nativeCache.putWithWriter(new Element(1, "test"));
        System.out.println("cache get:" + cache.get(1).get());
        System.out.println("cache evict:" + 1);
        nativeCache.removeWithWriter(1);
    }
}
