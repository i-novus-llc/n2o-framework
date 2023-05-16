package net.n2oapp.cache.template;

import net.n2oapp.context.StaticSpringContext;
import net.sf.ehcache.Element;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * User: iryabov
 * Date: 30.03.13
 * Time: 10:52
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("test-two-level-cache-context.xml")
public class TwoLevelCacheTemplateTest {

    @Test
    void testFirstCacheMissAndSecondCacheMiss() {
        CacheManager cacheManager = StaticSpringContext.getBean(CacheManager.class);
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
        assert "test".equals(value);
    }

    @Test
    void testFirstCacheMissAndSecondCacheHit() {
        CacheManager cacheManager = StaticSpringContext.getBean(CacheManager.class);
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
        assert "test".equals(value);
    }

    @Test
    void testFirstCacheHit() {
        CacheManager cacheManager = StaticSpringContext.getBean(CacheManager.class);
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
        assert "test".equals(value);
    }

    @Test
    @Disabled
    void testWriteBehindPutSyncEvictSync() throws InterruptedException {
        CacheManager cacheManager = StaticSpringContext.getBean(CacheManager.class);
        Cache cache = cacheManager.getCache("writeBehindCache");
        net.sf.ehcache.Cache nativeCache = (net.sf.ehcache.Cache) cache.getNativeCache();
        System.out.println("cache put:" + "test");
        nativeCache.putWithWriter(new Element(1, "test"));
        System.out.println("cache get:" + cache.get(1).get());
        System.out.println("sleep:" + 2 + "s");
        Thread.sleep(2000);
        System.out.println("cache evict:" + 1);
        System.out.println("cache get:" + cache.get(1).get());
        nativeCache.removeWithWriter(1);
        System.out.println("cache get:" + cache.get(1));
        System.out.println("sleep:" + 2 + "s");
        Thread.sleep(2000);
    }

    @Test
    @Disabled
    void testWriteBehindPutEvictSync() throws InterruptedException {
        CacheManager cacheManager = StaticSpringContext.getBean(CacheManager.class);
        Cache cache = cacheManager.getCache("writeBehindCache");
        net.sf.ehcache.Cache nativeCache = (net.sf.ehcache.Cache) cache.getNativeCache();
        System.out.println("cache put:" + "test");
        nativeCache.putWithWriter(new Element(1, "test"));
        System.out.println("cache get:" + cache.get(1).get());
        System.out.println("cache evict:" + 1);
        nativeCache.removeWithWriter(1);
        System.out.println("sleep:" + 2 + "s");
        Thread.sleep(2000);
    }
}
