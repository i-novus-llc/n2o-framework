package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.datasource.BrowserStorageDatasourceIO;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи действия submit
 */
public class SubmitActionElementIOV2Test {

    @Test
    public void testAlertActionElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new SubmitActionElementIOV2(), new BrowserStorageDatasourceIO());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testSubmitActionElementIOV2Test.page.xml");
    }
}
