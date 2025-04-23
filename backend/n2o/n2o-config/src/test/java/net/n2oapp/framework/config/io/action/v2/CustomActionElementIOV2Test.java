package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи элемента <action>
 */
class CustomActionElementIOV2Test {

    @Test
    void testInvokeActionElementIOV2Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new CustomActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testCustomActionElementIO.page.xml");
    }
}
