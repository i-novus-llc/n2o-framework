package net.n2oapp.framework.config.io.action.v1;


import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи элемента <invoke-action>
 */
public class InvokeActionElementIOV1Test {

    @Test
    public void testInvokeActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new InvokeActionElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/action/v1/testInvokeActionElementIOV1.page.xml");
    }
}