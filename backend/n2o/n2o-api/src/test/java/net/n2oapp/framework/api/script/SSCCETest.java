package net.n2oapp.framework.api.script;

import javax.script.*;

import org.junit.Ignore;
import org.junit.Test;
import net.n2oapp.framework.api.util.async.MultiThreadRunner;

import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

import java.util.ArrayList;

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
    @Ignore
    public void testThreadNotSafety() throws Exception {
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
    @Ignore
    public void testThreadSafety() throws Exception {
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