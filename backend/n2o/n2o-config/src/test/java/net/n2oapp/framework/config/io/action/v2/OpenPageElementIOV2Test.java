package net.n2oapp.framework.config.io.action.v2;


import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <open-page>
 */
public class OpenPageElementIOV2Test {

    @Test
    public void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new OpenPageElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testOpenPageElementIOV2.page.xml");
    }
}