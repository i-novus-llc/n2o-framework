package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.OpenDrawerElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест чтения/записи действия открытия drawer окна
 */
public class OpenDrawerElementIOV1Test {

    @Test
    public void testOpenDrawerElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new OpenDrawerElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testOpenDrawerElementIOV1.page.xml");
    }
}