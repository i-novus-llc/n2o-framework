package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Проверка чтения и персиста события <close>
 */
class CloseActionElementIOV2Test {
    
    @Test
    void testCloseActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new CloseActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/testCloseActionElementIOV2.page.xml");
    }
}