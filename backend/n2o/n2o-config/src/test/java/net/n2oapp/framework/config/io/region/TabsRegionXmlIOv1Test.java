package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.metadata.pack.N2oWidgetsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи региона в виде вкладок версии 1.0
 */
public class TabsRegionXmlIOv1Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TabsRegionIOv1());
        tester.addPack(new N2oWidgetsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/region/testTabsRegionXmlIOv1.region.xml");
    }
}
