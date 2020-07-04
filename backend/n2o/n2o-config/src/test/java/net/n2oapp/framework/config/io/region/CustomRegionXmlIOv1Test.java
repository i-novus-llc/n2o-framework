package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class CustomRegionXmlIOv1Test {
    @Test
    public void testCustomRegionXmlIOv1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomRegionIOv1());

        assert tester.check("net/n2oapp/framework/config/io/region/testCustomRegionXmlIOv1.region.xml");
    }
}
