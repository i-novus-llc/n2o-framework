package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Проверка чтения и персиста события <close>
 */
public class CloseActionElementIOV2Test {
    @Test
    public void testCloseActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new CloseActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testCloseActionElementIOV2.page.xml");
    }
}