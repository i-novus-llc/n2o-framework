package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <a>
 */
public class AnchorElementIOV1Test {
    @Test
    public void testAnchorElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new AnchorElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testAnchorActionElementIOV1.page.xml");
    }
}