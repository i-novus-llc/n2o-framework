package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Проверка чтения/записи события <copy>
 */
class CopyActionElementIOV2Test {
    @Test
    void testCopyActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new CopyActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testCopyActionElementIOV2.page.xml");
    }
}