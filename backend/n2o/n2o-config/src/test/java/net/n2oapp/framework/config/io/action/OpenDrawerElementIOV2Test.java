package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тест чтения/записи действия открытия drawer окна
 */
class OpenDrawerElementIOV2Test {

    @Test
    void testOpenDrawerElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new OpenDrawerElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/testOpenDrawerElementIOV2.page.xml");
    }
}