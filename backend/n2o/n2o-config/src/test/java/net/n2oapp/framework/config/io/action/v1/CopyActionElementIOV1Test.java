package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.CopyActionElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Проверка чтения/записи события <copy>
 */
class CopyActionElementIOV1Test {
    
    @Test
    void testCopyActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new CopyActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testCopyActionElementIOV1.page.xml");
    }
}