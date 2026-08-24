package net.n2oapp.framework.api.script;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Операция над движком скриптов, взятым из пула методом {@link ScriptProcessor#withEngine(EngineFunction)}.
 *
 * @param <T> тип результата операции
 */
@FunctionalInterface
public interface EngineFunction<T> {

    T apply(ScriptEngine engine) throws ScriptException;
}
