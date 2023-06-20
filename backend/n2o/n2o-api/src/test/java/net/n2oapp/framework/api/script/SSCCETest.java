package net.n2oapp.framework.api.script;

import javax.script.*;

import net.n2oapp.framework.api.util.async.MultiThreadRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SSCCETest {
    private final static ScriptEngineManager engineMgr = new ScriptEngineManager();

    private static final String script = new StringBuilder("i = 0;")
            .append("i += 1;")
            .append("shortly_later = new Date()/1000 + Math.random;") // 0..1 sec later
            .append("while( (new Date()/1000) < shortly_later) { Math.random() };") //prevent optimizations
            .append("i += 1;")
            .toString();

    /**
     * Пример вызова скрипта в потоко не безопасном режиме.
     * При вызове используется глобальный скоуп переменных, и он шаринтся между потоками.
     */
    @Test
    @Disabled
    void testThreadNotSafety() throws Exception {
        final Compilable engine = (Compilable) engineMgr.getEngineByName("JavaScript");
        final CompiledScript onePlusOne = engine.compile(script);
        MultiThreadRunner runner = new MultiThreadRunner();
        assert runner.run(() -> {
            try {
                return ((Integer) 2).equals(onePlusOne.eval());
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }) == 0;
    }

    /**
     * Пример вызова скрипта потокобезопасно.
     * Отличие от небезопасного в том, что при вызове добавляется локальный скоуп переменных.
     */
    @Test
    @Disabled
    void testThreadSafety() throws Exception {
        final Compilable engine = (Compilable) engineMgr.getEngineByName("JavaScript");
        final CompiledScript onePlusOne = engine.compile(script);
        MultiThreadRunner runner = new MultiThreadRunner();
        assert runner.run(() -> {
            try {
                return ((Integer) 2).equals(onePlusOne.eval(new SimpleBindings()));
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }) == 0;
    }
}