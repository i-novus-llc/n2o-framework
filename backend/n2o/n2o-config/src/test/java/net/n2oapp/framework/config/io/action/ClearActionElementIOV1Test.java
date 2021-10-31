package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Проверка чтения и персиста события <clear>
 */
public class ClearActionElementIOV1Test {
    @Test
    public void testClearActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new ClearActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/testClearActionElementIOV1.page.xml");
    }
}