package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Проверка чтения и персиста события <close>
 */
public class CloseActionElementIOV1Test {
    @Test
    public void testCloseActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new CloseActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testCloseActionElementIOV1.page.xml");
    }
}