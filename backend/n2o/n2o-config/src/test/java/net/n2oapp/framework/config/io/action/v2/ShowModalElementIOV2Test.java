package net.n2oapp.framework.config.io.action.v2;


import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи модального окна
 */
public class ShowModalElementIOV2Test {

    @Test
    public void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new ShowModalElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testShowModalElementIOV2.page.xml");
    }
}