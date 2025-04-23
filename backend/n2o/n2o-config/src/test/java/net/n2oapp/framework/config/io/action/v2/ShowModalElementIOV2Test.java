package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи модального окна
 */
class ShowModalElementIOV2Test {

    @Test
    void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardDatasourceIO(), new StandardPageElementIOv4(), new ShowModalElementIOV2(),
                new ButtonIOv2(), new InvokeActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testShowModalElementIOV2.page.xml");
    }
}