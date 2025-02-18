package net.n2oapp.cache.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование класса {@link SyncCacheTemplate}
 */
class SyncCacheTemplateTest {

    private CacheManager cacheManager;
    private MockCache cache;

    @BeforeEach
    public void setUp() {
        cacheManager = mock(CacheManager.class);
        cache = new MockCache();
        when(cacheManager.getCache("region")).thenReturn(cache);
    }

    @Test
    void testBase() {
        CacheTemplate<String, String> cacheTemplate = new SyncCacheTemplate<>(cacheManager);

        // холодный
        cache.clearStatistics();
        assertEquals("value", cacheTemplate.execute("region", "key", () -> "value"));
        // синхронные кэши делают 2 промаха: один когда обращаются не синхронно, второй в синхронной секции
        assertEquals(2, cache.getMiss());
        assertEquals(1, cache.getPut());
        assertEquals(0, cache.getHit());

        // горячий
        cache.clearStatistics();
        assertEquals("value", cacheTemplate.execute("region", "key", () -> "value"));
        assertEquals(0, cache.getMiss());
        assertEquals(0, cache.getPut());
        assertEquals(1, cache.getHit());
    }

    @Test
    void testAsync() {
        CacheTemplate<String, String> cacheTemplate = new SyncCacheTemplate<>(cacheManager);

        Set<Thread> threads = new HashSet<>();
        final int max = 10;
        Runnable test = () -> cacheTemplate.execute("region", "key", () -> "value" + Thread.currentThread().getId());
        IntStream.range(0, max).forEach(i -> threads.add(new Thread(test)));

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals(1, cache.getPut());
        assertEquals(max - 1, cache.getHit());
        // Поток, который вставил значение в кэш, промахнулся 2 раза: сначала до synchronized, потом - внутри synchronized.
        // Остальные потоки могли не промахнуться, если на тот момент в кэше было значение.
        // Но если промахнулись, то только по разу, до synchronized, внутри synchronized - у них попадание
        assertTrue(cache.getMiss() >= 2);
        assertTrue(cache.getMiss() <= 2 + (max - 1));
    }
}
