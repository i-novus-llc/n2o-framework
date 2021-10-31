package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи элемента <invoke-action>
 */
public class InvokeActionElementIOV1Test {

    @Test
    public void testInvokeActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new InvokeActionElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/action/testInvokeActionElementIOV1.page.xml");
    }
}