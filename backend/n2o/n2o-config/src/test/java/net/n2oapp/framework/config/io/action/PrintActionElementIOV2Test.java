package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения/записи действия печати
 */
class PrintActionElementIOV2Test {
    
    @Test
    void testPrintActionElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new PrintActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/testPrintActionElementIOV2.page.xml");
    }
}