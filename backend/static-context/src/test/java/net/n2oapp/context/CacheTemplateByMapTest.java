package net.n2oapp.context;

import org.junit.jupiter.api.Test;

/**
 * @author operehod
 * @since 13.05.2015
 */
public class CacheTemplateByMapTest {


    @Test
    public void test() throws Exception {
        CacheTemplateByMap cacheTemplate = new CacheTemplateByMap();
        assert cacheTemplate.get(() -> 1, "one") == 1;
        assert cacheTemplate.get(() -> 2, "one") == 1;
        //ошибки нету, так как код не должен вызываться
        assert cacheTemplate.get(() -> errorInteger(), "one") == 1;
        assert cacheTemplate.get(CacheTemplateByMapTest::errorInteger, "one") == 1;

        //ошибка есть, так как код постоянно вызывается, кэша нету
        assertOnException(() -> cacheTemplate.get(() -> errorInteger(), "two"), RuntimeException.class);
        assertOnException(() -> cacheTemplate.get(() -> errorInteger(), "two"), RuntimeException.class);

        assert cacheTemplate.get(() -> 2, "two") == 2;
        assert cacheTemplate.get(() -> errorInteger(), "one") == 1;
        assert cacheTemplate.get(() -> errorInteger(), "two") == 2;
    }


    @Test
    public void testManyKeys() throws Exception {
        CacheTemplateByMap cacheTemplate = new CacheTemplateByMap();
        assert cacheTemplate.get(() -> 1, "one", Integer.class) == 1;
        assert cacheTemplate.get(() -> "1", "one", String.class).equals("1");
        assert cacheTemplate.get(() -> 0, "one", Integer.class) == 1;
        assert cacheTemplate.get(() -> "", "one", String.class).equals("1");

    }

    public static Integer errorInteger() {
        throw new RuntimeException();
    }


    //копипаст
    public static void assertOnException(Closure closure, Class<? extends Exception> clazz) {
        boolean result = false;
        try {
            closure.call();
        } catch (Exception e) {
            result = clazz.isAssignableFrom(e.getClass());
        }
        assert result;
    }


    public interface Closure {
        public void call();
    }


}
