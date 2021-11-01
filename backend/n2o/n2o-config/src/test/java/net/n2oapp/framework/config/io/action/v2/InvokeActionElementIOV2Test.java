package net.n2oapp.framework.config.io.action.v2;


import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи элемента <invoke-action>
 */
public class InvokeActionElementIOV2Test {

    @Test
    public void testInvokeActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new InvokeActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testInvokeActionElementIOV2.page.xml");
    }
}