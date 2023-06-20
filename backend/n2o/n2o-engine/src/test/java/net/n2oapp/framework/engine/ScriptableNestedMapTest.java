package net.n2oapp.framework.engine;

import net.n2oapp.criteria.dataset.NestedMap;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * User: iryabov
 * Date: 28.05.2014
 * Time: 10:58
 */
public class ScriptableNestedMapTest {
    private ScriptEngineManager engineMgr = new ScriptEngineManager();

    @Test
    void testGetMap() throws Exception {
        ScriptEngine scriptEngine = engineMgr.getEngineByName("JavaScript");
        NestedMap map = new NestedMap();
        map.put("b.c", 1);
        scriptEngine.put("a", map);
        Object val = scriptEngine.eval("a.b.c");
        System.out.println(val);
        assert Integer.valueOf(1) == val;
    }

    @Test
    void testGetList() throws Exception {
        ScriptEngine scriptEngine = engineMgr.getEngineByName("JavaScript");
        NestedMap map = new NestedMap();
        map.put("list[0]", 1);
        map.put("list[1]", new HashMap<>());
        map.put("list[1].b", 10);
        scriptEngine.put("a",map);
        Object val = scriptEngine.eval("a.list");
        assert val instanceof Collection;
        assert 2 == ((Collection) val).size();
        val = scriptEngine.eval("a.list[0]");
        assert Integer.valueOf(1) == val;
        val = scriptEngine.eval("a.list[1].b");
        assert Integer.valueOf(10) == val;
    }

    @Test
    void testPutGetList() throws Exception {
        ScriptEngine scriptEngine = engineMgr.getEngineByName("JavaScript");
        scriptEngine.put("list", Arrays.asList(1, 2, 10).toArray());
        Object val = scriptEngine.eval("list[0]");
        assert Integer.valueOf(1) == val;
        val = scriptEngine.eval("list[1]");
        assert Integer.valueOf(2) == val;
        val = scriptEngine.eval("list[2]");
        assert Integer.valueOf(10) == val;
    }

    @Test
    void testUndefined() throws Exception {
        ScriptEngine scriptEngine = engineMgr.getEngineByName("JavaScript");
        NestedMap map = new NestedMap();
        scriptEngine.put("a", map);
        Boolean val = (Boolean) scriptEngine.eval("typeof b == 'undefined'");
        assert val;
    }
}
