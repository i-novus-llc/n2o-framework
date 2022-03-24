package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.PerformElementIOv1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
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
                .ios(new StandardPageElementIOv3(), new ButtonIO(), new PerformElementIOv1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testPerformElementIOv1.page.xml");
    }
}