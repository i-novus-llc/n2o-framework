package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтение/записи действия <switch> версии 2.0
 */
public class SwitchActionElementIOV2Test {

    @Test
    void testSwitchActionElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new SwitchActionElementIOV2(), new InvokeActionElementIOV2(),
                new AnchorElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testSwitchActionElementIOV2.page.xml");
    }
}
