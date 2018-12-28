package net.n2oapp.properties.test;

import java.util.function.Consumer;

/**
 * @author operehod
 * @since 13.04.2015
 */
public class TestUtil {

    public static void assertOnException(Closure closure, Class<? extends Exception> clazz) {
        assertOnException(closure, clazz, e -> {
        });
    }


    @SuppressWarnings("unchecked")
    public static <T extends Exception> void assertOnException(Closure closure, Class<T> clazz, Consumer<T> checker) {
        boolean result = false;
        try {
            closure.call();
        } catch (Exception e) {
            result = clazz.isAssignableFrom(e.getClass());
            checker.accept((T) e);
        }
        assert result;
    }



    public static void assertOnSuccess(Closure closure) {
        boolean result = true;
        try {
            closure.call();
        } catch (Exception e) {
            result = false;
        }
        assert result;
    }

    public interface Closure {

        void call();

    }



}
