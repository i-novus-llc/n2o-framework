package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тест на чтение/запись элемента <a>
 */
class AnchorElementIOV2Test {
    
    @Test
    void testAnchorElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new AnchorElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testAnchorActionElementIOV2.page.xml");
    }
}