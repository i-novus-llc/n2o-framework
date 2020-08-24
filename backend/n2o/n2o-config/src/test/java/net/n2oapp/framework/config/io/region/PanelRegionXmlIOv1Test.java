package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи региона в виде в виде панелей версии 1.0
 */
public class PanelRegionXmlIOv1Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new PanelRegionIOv1());

        assert tester.check("net/n2oapp/framework/config/io/region/testPanelRegionXmlIOv1.region.xml");
    }
}
