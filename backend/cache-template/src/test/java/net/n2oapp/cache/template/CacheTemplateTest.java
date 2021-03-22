package net.n2oapp.cache.template;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cache.CacheManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тест класса {@link CacheTemplate}
 */
public class CacheTemplateTest {

    private CacheManager cacheManager;
    private MockCache cache;

    /**
     * Создание заглушки кэш менеджера и кэш провайдера
     */
    @Before
    public void setUp() throws Exception {
        cacheManager = mock(CacheManager.class);
        cache = new MockCache();
        when(cacheManager.getCache("test")).thenReturn(cache);
    }

    /**
     * Проверка, что шаблон для кэширования вызывает {@link CacheCallback}
     * только в случае отсутствия записи в кэше.
     */
    @Test
    public void testBase() throws Exception {
        CacheTemplate<String, String> cacheTemplate = new CacheTemplate<>(cacheManager);

        //"test1" нет в кэше, вызывается callback
        assertEquals("value1", cacheTemplate.execute("test", "test1", () -> "value1"));
        assertEquals(1, cache.getMiss());//попытались взять из кэша
        assertEquals(1, cache.getPut());//вставили в кэш
        assertEquals(0, cache.getHit());

        cache.clearStatistics();
        //"test1" есть в кэше, callback не вызывается
        assertEquals("value1", cacheTemplate.execute("test", "test1", () -> "fail"));
        assertEquals(0, cache.getMiss());
        assertEquals(0, cache.getPut());
        assertEquals(1, cache.getHit());//взяли из кэша

    }
}
