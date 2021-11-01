package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <perform>
 */
public class PerformElementIOv2Test {
    @Test
    public void testPerformElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new StandardPageElementIOv4(), new ButtonIOv2(), new PerformElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testPerformElementIOv2.page.xml");
    }
}