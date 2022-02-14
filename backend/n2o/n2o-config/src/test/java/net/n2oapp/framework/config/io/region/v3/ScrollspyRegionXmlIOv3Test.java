package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.config.metadata.pack.N2oRegionsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class ScrollspyRegionXmlIOv3Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addPack(new N2oRegionsV3IOPack())
                .addPack(new N2oWidgetsV5IOPack());

        assert tester.check("net/n2oapp/framework/config/io/region/v3/testScrollspyRegionXmlIOv3.region.xml");
    }
}
