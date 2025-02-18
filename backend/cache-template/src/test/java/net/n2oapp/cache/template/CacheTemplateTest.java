package net.n2oapp.cache.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование класса {@link CacheTemplate}
 */
@ExtendWith(OutputCaptureExtension.class)
class CacheTemplateTest {

    private CacheManager cacheManager;
    private MockCache cache;

    /**
     * Создание заглушки кэш менеджера и кэш провайдера
     */
    @BeforeEach
    void setUp() {
        cacheManager = mock(CacheManager.class);
        cache = new MockCache();
        when(cacheManager.getCache("region")).thenReturn(cache);
    }

    /**
     * Проверка, что шаблон для кэширования вызывает {@link CacheCallback}
     * только в случае отсутствия записи в кэше.
     */
    @Test
    void testBase(CapturedOutput output) {
        CacheTemplate<String, String> cacheTemplate = new CacheTemplate<>(cacheManager);

        // "test1" нет в кэше, вызывается callback
        assertEquals("value", cacheTemplate.execute("region", "key", () -> "value"));
        assertEquals(1, cache.getMiss());   //попытались взять из кэша
        assertEquals(1, cache.getPut());    //вставили в кэш
        assertEquals(0, cache.getHit());

        cache.clearStatistics();
        // "test1" есть в кэше, callback не вызывается
        assertEquals("value", cacheTemplate.execute("region", "key", () -> "fail"));
        assertEquals(0, cache.getMiss());
        assertEquals(0, cache.getPut());
        assertEquals(1, cache.getHit());    //взяли из кэша

        cache.clearStatistics();
        assertEquals("value", cacheTemplate.execute("unknown", "key", () -> "value"));
        assertEquals(0, cache.getMiss());
        assertEquals(0, cache.getPut());
        assertEquals(0, cache.getHit());
        assertTrue(output.getOut().contains("Не найдена область кэша с именем [unknown] для CacheTemplate"));
    }
}
