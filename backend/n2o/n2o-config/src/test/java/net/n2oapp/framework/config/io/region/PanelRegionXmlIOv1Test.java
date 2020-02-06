package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class PanelRegionXmlIOv1Test {
    @Test
    public void testPanelRegionXmlIOv1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new PanelRegionIOv1());

        assert tester.check("net/n2oapp/framework/config/io/region/testPanelRegionXmlIOv1.region.xml");
    }
}
