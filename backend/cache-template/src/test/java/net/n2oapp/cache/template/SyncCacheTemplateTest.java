package net.n2oapp.cache.template;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SyncCacheTemplateTest {

    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        cacheManager = mock(CacheManager.class);
    }

    @Test
    public void testBase() throws Exception {
        MockCache cache = new MockCache();
        when(cacheManager.getCache("test")).thenReturn(cache);
        CacheTemplate<String, String> cacheTemplate = new SyncCacheTemplate<>(cacheManager);

        //холодный
        cache.clearStatistics();
        assertEquals("value1", cacheTemplate.execute("test", "test1", () -> "value1"));
        assertEquals(2, cache.getMiss());//синхронные кэши делают 2 промаха: один когда обращаются не синхронно, второй в синхронной секции
        assertEquals(1, cache.getPut());
        assertEquals(0, cache.getHit());

        //горячий
        cache.clearStatistics();
        assertEquals("value1", cacheTemplate.execute("test", "test1", () -> "value1"));
        assertEquals(0, cache.getMiss());
        assertEquals(0, cache.getPut());
        assertEquals(1, cache.getHit());
    }

    @Test
    @Ignore
    public void testAsync() throws InterruptedException {
        MockCache cache = new MockCache();
        when(cacheManager.getCache("test")).thenReturn(cache);
        CacheTemplate<String, String> cacheTemplate = new SyncCacheTemplate<>(cacheManager);

        Runnable test = () -> {
            cacheTemplate.execute("test", "test1", () -> {
                try {
                    Thread.sleep(200);
                    return "value" + Thread.currentThread().getId();
                } catch (InterruptedException ignored) {
                    return null;
                }
            });
        };

        Set<Thread> threads = new HashSet<>();
        int max = 10;
        IntStream.range(0, max).forEach((i) -> {
            threads.add(new Thread(test));
        });
        threads.forEach(Thread::start);
        threads.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        });
        assertEquals(1, cache.getPut());
        assertEquals(max - 1, cache.getHit());
        assertEquals(2 + (max - 1), cache.getMiss());
        //2 раза промахнулся тот кто вставил (сначала до синхрона, потом во время синхрона)
        //(max - 1) все остальные вне синхрона, в синхроне у них попадание
    }

}
