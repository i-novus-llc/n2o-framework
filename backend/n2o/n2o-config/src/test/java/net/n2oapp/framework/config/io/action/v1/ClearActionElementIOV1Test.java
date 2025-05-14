package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.ClearActionElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Проверка чтения и персиста события <clear>
 */
class ClearActionElementIOV1Test {
    
    @Test
    void testClearActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new ClearActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testClearActionElementIOV1.page.xml");
    }
}