package net.n2oapp.framework.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Тесты DynamicUtil
 */
public class DynamicUtilTest {

    @Test
    public void testResolveTokens() {
        assertEquals("provider$_test", DynamicUtil.resolveTokens("provider$_{name}", "test"));
        assertEquals("provider$table_test", DynamicUtil.resolveTokens("provider$table_{name}", "test"));
        assertEquals("provider$_a,b,c", DynamicUtil.resolveTokens("provider$_{a},{b},{c}", "a", "b", "c"));
        assertEquals("provider$_a,b,a,b", DynamicUtil.resolveTokens("provider$_{a},{b},{a},{b}", "a", "b"));
        assertEquals("provider$_a,b,a,b", DynamicUtil.resolveTokens("provider$_{a},{b},{a},{b}", "a", "b", "c"));
        try {
            assertEquals("provider$_a,b,a,b", DynamicUtil.resolveTokens("provider$_{a},{b},{a},{b}", "a"));
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testHasRefs() {
        assertTrue(DynamicUtil.hasRefs("{name}"));
        assertTrue(DynamicUtil.hasRefs("table_{name}"));
        assertTrue(DynamicUtil.hasRefs("table_{name1}_{name2}"));
        assertFalse(DynamicUtil.hasRefs("table_name"));
    }
}
