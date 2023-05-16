package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.DynamicUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;
import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnSuccess;

/**
 * @author operehod
 * @since 17.04.2015
 */
public class DynamicMetadataUtilTest {

    @Test
    void testIsDynamic() {
        assert DynamicUtil.isDynamic("static?dynamic");
        assert DynamicUtil.isDynamic("static?complex?dynamic");
        assert !DynamicUtil.isDynamic("static");
    }


    @Test
    void testCheckDynamicIds() {
        assertOnSuccess(() -> DynamicUtil.checkDynamicIds(Arrays.asList("static?dynamic", "static?complex?dynamic", "test?test"), ""));
        assertOnException(() -> DynamicUtil.checkDynamicIds(Arrays.asList("static?dynamic", "static?complex?dynamic", "static"), ""), RuntimeException.class);

    }
}
