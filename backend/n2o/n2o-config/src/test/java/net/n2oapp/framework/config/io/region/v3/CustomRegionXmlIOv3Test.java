package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи кастомного региона версии 3.0
 */
public class CustomRegionXmlIOv3Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomRegionIOv3(), new PanelRegionIOv3());
        tester.addPack(new N2oWidgetsV5IOPack());

        assert tester.check("net/n2oapp/framework/config/io/region/v3/testCustomRegionXmlIOv3.region.xml");
    }
}
