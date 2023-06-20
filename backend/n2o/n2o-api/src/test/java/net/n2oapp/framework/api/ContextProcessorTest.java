package net.n2oapp.framework.api;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.exception.NotFoundContextPlaceholderException;
import net.n2oapp.framework.api.context.ContextProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;

/**
 * Тесты процессора контекста
 */
public class ContextProcessorTest {
    
    private ContextProcessor processor;

    @BeforeEach
    public void setUp() throws Exception {
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

        }
    }

    @Test
    void testResolve() {
        assert processor.resolve(null) == null;
        assert processor.resolve("").equals("");
        assert processor.resolve("bla bla").equals("bla bla");
        assert processor.resolve("#{name?}").equals("oleg");
        assert processor.resolve("#{name}").equals("oleg");
        assert processor.resolve("#{name!}").equals("oleg");
        assertOnException(() -> processor.resolve("#{surname!}"), NotFoundContextPlaceholderException.class, e -> {
            assert "n2o.fieldNotFoundInContext".equals(e.getUserMessage());
            assert "surname".equals(e.getData());
        });
        assert processor.resolve("#{surname}") == null;
        assert processor.resolve("#{surname?}") == null;
        assert processor.resolve("#{surname?none}").equals("none");
        assert processor.resolve(Arrays.asList(1, 2, "#{three}")).equals(Arrays.asList(1, 2, 3));

        assertOnException(() -> processor.resolve("#{empty!}"), NotFoundContextPlaceholderException.class, e -> {
            assert "n2o.fieldNotFoundInContext".equals(e.getUserMessage());
            assert "empty".equals(e.getData());
        });
        assert processor.resolve("#{empty?val}").equals("val");
    }

    @Test
    void hasContext() {
        assert processor.hasContext("aaa #{name} bbb");
        assert processor.hasContext("aaa #{surname} bbb");
        assert !processor.hasContext(null);
        assert !processor.hasContext("");
        assert !processor.hasContext("aaaa");
        assert !processor.hasContext("{name}");
        assert !processor.hasContext("}#{name");
        assert !processor.hasContext("${name!}");
    }

    @Test
    void testNotExpression() {
        assert processor.resolve("#name}").equals("#name}");
        assert processor.resolve("}#{name").equals("}#{name");
        assert processor.resolve("name:{name}").equals("name:{name}");
    }

    @Test
    void testResolveText() {
        assert processor.resolveText("text").equals("text");
        assert processor.resolveText("#{name}").equals("oleg");
        assert processor.resolveText("#{surname}").equals("");
        assert processor.resolveText("#{surname?}").equals("");
        assert processor.resolveText("name:#{name}").equals("name:oleg");
        assert processor.resolveText("name:#{name?}").equals("name:oleg");
        assert processor.resolveText("name:#{name?none}").equals("name:oleg");
        assert processor.resolveText("name:#{unknown}").equals("name:");
        assert processor.resolveText("name:#{unknown?none}").equals("name:none");
        assert processor.resolveText("name:#{name} surname:#{surname?none}").equals("name:oleg surname:none");

        assertOnException(() -> processor.resolveText("#{empty!}"), NotFoundContextPlaceholderException.class);
        assert processor.resolveText("#{empty?val}").equals("val");
    }

}
