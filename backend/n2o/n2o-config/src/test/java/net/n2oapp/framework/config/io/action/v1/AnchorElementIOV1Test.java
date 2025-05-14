package net.n2oapp.framework.config.io.action.v1;


import net.n2oapp.framework.config.io.action.AnchorElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тест на чтение/запись элемента <a>
 */
class AnchorElementIOV1Test {
    
    @Test
    void testAnchorElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new AnchorElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testAnchorActionElementIOV1.page.xml");
    }
}