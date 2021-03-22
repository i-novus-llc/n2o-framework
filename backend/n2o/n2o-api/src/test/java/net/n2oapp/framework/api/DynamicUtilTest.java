package net.n2oapp.framework.api;

import org.junit.Assert;
import org.junit.Test;

/**
 * Тесты DynamicUtil
 */
public class DynamicUtilTest {

    @Test
    public void testResolveTokens() throws Exception {
        Assert.assertEquals("provider$_test", DynamicUtil.resolveTokens("provider$_{name}", "test"));
        Assert.assertEquals("provider$table_test", DynamicUtil.resolveTokens("provider$table_{name}", "test"));
        Assert.assertEquals("provider$_a,b,c", DynamicUtil.resolveTokens("provider$_{a},{b},{c}", "a", "b", "c"));
        Assert.assertEquals("provider$_a,b,a,b", DynamicUtil.resolveTokens("provider$_{a},{b},{a},{b}", "a", "b"));
        Assert.assertEquals("provider$_a,b,a,b", DynamicUtil.resolveTokens("provider$_{a},{b},{a},{b}", "a", "b", "c"));
        try {
            Assert.assertEquals("provider$_a,b,a,b", DynamicUtil.resolveTokens("provider$_{a},{b},{a},{b}", "a"));
            Assert.fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testHasRefs() throws Exception {
        Assert.assertTrue(DynamicUtil.hasRefs("{name}"));
        Assert.assertTrue(DynamicUtil.hasRefs("table_{name}"));
        Assert.assertTrue(DynamicUtil.hasRefs("table_{name1}_{name2}"));
        Assert.assertFalse(DynamicUtil.hasRefs("table_name"));
    }
}
