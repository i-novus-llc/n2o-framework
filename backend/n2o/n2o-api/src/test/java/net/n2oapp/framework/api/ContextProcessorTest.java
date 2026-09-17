package net.n2oapp.framework.api;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.exception.NotFoundContextPlaceholderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты процессора контекста
 */
class ContextProcessorTest {

    private ContextProcessor processor;

    @BeforeEach
    void setUp() {
        Context service = new ContextMock();
        processor = new ContextProcessor(service);
    }

    private static class ContextMock implements Context {

        @Override
        public Object get(String name) {
            if ("name".equals(name)) return "oleg";
            if ("three".equals(name)) return 3;
            if ("empty".equals(name)) return "";
            return null;
        }

        @Override
        public void set(Map<String, Object> dataSet) {
            // this method is empty
        }
    }

    @Test
    void testResolve() {
        assertNull(processor.resolve(null));
        assertEquals("", processor.resolve(""));
        assertEquals("bla bla", processor.resolve("bla bla"));
        assertEquals("oleg", processor.resolve("#{name?}"));
        assertEquals("oleg", processor.resolve("#{name}"));
        assertEquals("oleg", processor.resolve("#{name!}"));
        assertOnException(() -> processor.resolve("#{surname!}"), NotFoundContextPlaceholderException.class, e -> {
            assertEquals("n2o.fieldNotFoundInContext", e.getUserMessage());
            assertEquals("surname", e.getData());
        });
        assertNull(processor.resolve("#{surname}"));
        assertNull(processor.resolve("#{surname?}"));
        assertEquals("none", processor.resolve("#{surname?none}"));
        assertEquals(Arrays.asList(1, 2, 3), processor.resolve(Arrays.asList(1, 2, "#{three}")));

        assertOnException(() -> processor.resolve("#{empty!}"), NotFoundContextPlaceholderException.class, e -> {
            assertEquals("n2o.fieldNotFoundInContext", e.getUserMessage());
            assertEquals("empty", e.getData());
        });
        assertEquals("val", processor.resolve("#{empty?val}"));
    }

    @Test
    void hasContext() {
        assertTrue(processor.hasContext("aaa #{name} bbb"));
        assertTrue(processor.hasContext("aaa #{surname} bbb"));
        assertFalse(processor.hasContext(null));
        assertFalse(processor.hasContext(""));
        assertFalse(processor.hasContext("aaaa"));
        assertFalse(processor.hasContext("{name}"));
        assertFalse(processor.hasContext("}#{name"));
        assertFalse(processor.hasContext("${name!}"));
    }

    @Test
    void testNotExpression() {
        assertEquals("#name}", processor.resolve("#name}"));
        assertEquals("}#{name", processor.resolve("}#{name"));
        assertEquals("name:{name}", processor.resolve("name:{name}"));
    }

    @Test
    void testResolveText() {
        assertEquals("text", processor.resolveText("text"));
        assertEquals("oleg", processor.resolveText("#{name}"));
        assertEquals("", processor.resolveText("#{surname}"));
        assertEquals("", processor.resolveText("#{surname?}"));
        assertEquals("name:oleg", processor.resolveText("name:#{name}"));
        assertEquals("name:oleg", processor.resolveText("name:#{name?}"));
        assertEquals("name:oleg", processor.resolveText("name:#{name?none}"));
        assertEquals("name:", processor.resolveText("name:#{unknown}"));
        assertEquals("name:none", processor.resolveText("name:#{unknown?none}"));
        assertEquals("name:oleg surname:none", processor.resolveText("name:#{name} surname:#{surname?none}"));

        assertOnException(() -> processor.resolveText("#{empty!}"), NotFoundContextPlaceholderException.class);
        assertEquals("val", processor.resolveText("#{empty?val}"));
    }

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


    public interface Closure {
        void call();
    }
}