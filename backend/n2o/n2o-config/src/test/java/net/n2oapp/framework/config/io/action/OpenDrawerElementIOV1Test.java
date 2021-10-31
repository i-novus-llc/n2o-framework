package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест чтения/записи действия открытия drawer окна
 */
public class OpenDrawerElementIOV1Test {

    @Test
    public void testOpenDrawerElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new OpenDrawerElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testOpenDrawerElementIOV1.page.xml");
    }
}