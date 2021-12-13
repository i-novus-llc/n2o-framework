package net.n2oapp.framework.config.io.action.v2;


import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи действия печати
 */
public class PrintActionElementIOV2Test {
    @Test
    public void testPrintActionElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new PrintActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testPrintActionElementIOV2.page.xml");
    }
}