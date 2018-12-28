package net.n2oapp.framework.api.util;

import net.n2oapp.framework.api.exception.N2oException;

import java.util.function.Consumer;

/**
 * @author operehod
 * @since 13.04.2015
 */
public class N2oTestUtil {

    public static void assertOnException(Closure closure, Class<? extends Exception> clazz) {
        assertOnException(closure, clazz, e -> {
        });
    }

    public static void assertOnN2oException(Closure closure) {
        assertOnException(closure, N2oException.class);
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

}
