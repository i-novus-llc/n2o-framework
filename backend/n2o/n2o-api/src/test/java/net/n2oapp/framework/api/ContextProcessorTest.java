package net.n2oapp.framework.api;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.exception.NotFoundContextPlaceholderException;
import net.n2oapp.framework.api.context.ContextProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;

/**
 * Тесты процессора контекста
 */
public class ContextProcessorTest {
    
    private ContextProcessor processor;

    @Before
    public void setUp() throws Exception {
        Context service = new ContextMock();
        processor = new ContextProcessor(service);
    }

    private static class ContextMock implements Context {

        @Override
        public Object get(String name) {
            return name.equals("name") ? "oleg" : name.equals("three") ? 3 : null;
        }

        @Override
        public void set(Map<String, Object> dataSet) {

        }
    }

    @Test
    public void testResolve() throws Exception {
        assert processor.resolve(null) == null;
        assert processor.resolve("").equals("");
        assert processor.resolve("bla bla").equals("bla bla");
        assert processor.resolve("#{name?}").equals("oleg");
        assert processor.resolve("#{name}").equals("oleg");
        assert processor.resolve("#{name!}").equals("oleg");
        assertOnException(() -> processor.resolve("#{surname!}"), NotFoundContextPlaceholderException.class);
        assert processor.resolve("#{surname}") == null;
        assert processor.resolve("#{surname?}") == null;
        assert processor.resolve("#{surname?none}").equals("none");
        assert processor.resolve(Arrays.asList(1, 2, "#{three}")).equals(Arrays.asList(1, 2, 3));
    }

    @Test
    public void hasContext() throws Exception {
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
    public void testNotExpression() throws Exception {
        assert processor.resolve("#name}").equals("#name}");
        assert processor.resolve("}#{name").equals("}#{name");
        assert processor.resolve("name:{name}").equals("name:{name}");
    }

    @Test
    public void testResolveText() throws Exception {
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
    }

}
