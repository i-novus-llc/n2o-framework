package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
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

        assert tester.check("net/n2oapp/framework/config/io/action/testCustomActionElementIO.page.xml");
    }
}
