package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <open-page>
 */
public class OpenPageElementIOV1Test {

    @Test
    public void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new OpenPageElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testOpenPageElementIOV1.page.xml");
    }
}