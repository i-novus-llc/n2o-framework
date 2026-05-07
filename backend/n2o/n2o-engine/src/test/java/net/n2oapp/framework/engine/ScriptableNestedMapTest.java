package net.n2oapp.framework.engine;

import net.n2oapp.criteria.dataset.NestedMap;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * User: iryabov
 */
class ScriptableNestedMapTest {

    private static final HostAccess HOST_ACCESS = HostAccess.newBuilder()
            .allowMapAccess(true)
            .allowListAccess(true)
            .allowArrayAccess(true)
            .build();

    private Context createContext() {
        return Context.newBuilder("js")
                .allowHostAccess(HOST_ACCESS)
                .build();
    }

    @Test
    void testGetMap() throws Exception {
        NestedMap map = new NestedMap();
        map.put("b.c", 1);
        try (Context ctx = createContext()) {
            ctx.getBindings("js").putMember("a", map);
            Value val = ctx.eval("js", "a.b.c");
            assertEquals(1, val.asInt());
        }
    }

    @Test
    void testGetList() throws Exception {
        NestedMap map = new NestedMap();
        map.put("list[0]", 1);
        map.put("list[1]", new HashMap<>());
        map.put("list[1].b", 10);
        try (Context ctx = createContext()) {
            ctx.getBindings("js").putMember("a", map);
            Value list = ctx.eval("js", "a.list");
            assertTrue(list.hasArrayElements());
            assertEquals(2, list.getArraySize());
            assertEquals(1, ctx.eval("js", "a.list[0]").asInt());
            assertEquals(10, ctx.eval("js", "a.list[1].b").asInt());
        }
    }

    @Test
    void testPutGetList() throws Exception {
        try (Context ctx = createContext()) {
            ctx.getBindings("js").putMember("list", new Object[]{1, 2, 10});
            assertEquals(1, ctx.eval("js", "list[0]").asInt());
            assertEquals(2, ctx.eval("js", "list[1]").asInt());
            assertEquals(10, ctx.eval("js", "list[2]").asInt());
        }
    }

    @Test
    void testUndefined() throws Exception {
        NestedMap map = new NestedMap();
        try (Context ctx = createContext()) {
            ctx.getBindings("js").putMember("a", map);
            assertTrue(ctx.eval("js", "typeof b == 'undefined'").asBoolean());
        }
    }
}
