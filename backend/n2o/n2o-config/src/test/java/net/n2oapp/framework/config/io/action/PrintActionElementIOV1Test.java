package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи действия печати
 */
public class PrintActionElementIOV1Test {
    @Test
    public void testPrintActionElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new PrintActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testPrintActionElementIOV1.page.xml");
    }
}