package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи действия <alert>
 */
class AlertActionElementIOV2Test {

    @Test
    void testAlertActionElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new AlertActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/testAlertActionElementIOV2Test.page.xml");
    }
}
