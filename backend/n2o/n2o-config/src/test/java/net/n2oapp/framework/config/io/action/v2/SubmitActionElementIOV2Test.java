package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.datasource.BrowserStorageDatasourceIO;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи действия сохранения источника данных
 */
public class SubmitActionElementIOV2Test {

    @Test
    void testAlertActionElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new SubmitActionElementIOV2(), new BrowserStorageDatasourceIO());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testSubmitActionElementIOV2Test.page.xml");
    }
}
