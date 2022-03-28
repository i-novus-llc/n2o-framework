package net.n2oapp.framework.config.io.action.v1;


import net.n2oapp.framework.config.io.action.ShowModalElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи модального окна
 */
public class ShowModalElementIOV1Test {

    @Test
    public void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new ShowModalElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testShowModalElementIOV1.page.xml");
    }
}