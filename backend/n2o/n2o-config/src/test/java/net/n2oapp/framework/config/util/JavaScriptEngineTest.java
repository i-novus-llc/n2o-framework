package net.n2oapp.framework.config.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.junit.jupiter.api.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author iryabov
 * @since 17.04.2015
 */
class JavaScriptEngineTest {

    @Test
    void testEngine() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Bindings bindings = engine.createBindings();
        bindings.put("org", 1);
        Object value = engine.eval("org == 1", bindings);
        System.out.println(value);
        assertTrue((Boolean) value);
    }

    @Test
    void testScriptProcessor() throws ScriptException {
        DataSet dataSet = new DataSet();
        dataSet.put("org.id", 1);
        Object value = ScriptProcessor.eval("org.id == 1", dataSet);
        System.out.println(value);
        assertTrue((Boolean) value);
    }
}
