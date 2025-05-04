package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.DynamicUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;
import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnSuccess;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author operehod
 * @since 17.04.2015
 */
class DynamicMetadataUtilTest {

    @Test
    void testIsDynamic() {
        assertTrue(DynamicUtil.isDynamic("static?dynamic"));
        assertTrue(DynamicUtil.isDynamic("static?complex?dynamic"));
        assertFalse(DynamicUtil.isDynamic("static"));
    }

    @Test
    void testCheckDynamicIds() {
        assertOnSuccess(() -> DynamicUtil.checkDynamicIds(Arrays.asList("static?dynamic", "static?complex?dynamic", "test?test"), ""));
        assertOnException(() -> DynamicUtil.checkDynamicIds(Arrays.asList("static?dynamic", "static?complex?dynamic", "static"), ""), RuntimeException.class);
    }
}
