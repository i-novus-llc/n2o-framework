package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Проверка чтения и персиста события <close>
 */
public class CloseActionElementIOV1Test {
    @Test
    public void testCloseActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new CloseActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testCloseActionElementIOV1.page.xml");
    }
}