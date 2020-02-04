package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class TabsRegionXmlIOv1Test {
    @Test
    public void testTabsRegionXmlIOv1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TabsRegionIOv1());

        assert tester.check("net/n2oapp/framework/config/io/region/testTabsRegionXmlIOv1.region.xml");
    }
}
