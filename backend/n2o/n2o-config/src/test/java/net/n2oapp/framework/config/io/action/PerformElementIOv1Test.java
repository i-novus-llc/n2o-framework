package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <perform>
 */
public class PerformElementIOv1Test {
    @Test
    public void testPerformElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new StandardPageElementIOv2(), new ButtonIO(), new PerformElementIOv1());
        assert tester.check("net/n2oapp/framework/config/io/action/testPerformElementIOv1.page.xml");
    }
}