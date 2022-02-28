package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи действия <alert>
 */
public class AlertActionElementIOV2Test {

    @Test
    public void testAlertActionElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new AlertActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testAlertActionElementIOV2Test.page.xml");
    }
}
