package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи модального окна
 */
public class ShowModalElementIOV1Test {

    @Test
    public void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new ShowModalElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testShowModalElementIOV1.page.xml");
    }
}