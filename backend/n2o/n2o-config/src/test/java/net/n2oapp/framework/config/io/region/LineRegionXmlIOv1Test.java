package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class LineRegionXmlIOv1Test {
    @Test
    public void testLineRegionXmlIOv1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new LineRegionIOv1());

        assert tester.check("net/n2oapp/framework/config/io/region/testLineRegionXmlIOv1.region.xml");
    }
}
