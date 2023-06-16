package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.config.metadata.pack.N2oRegionsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи региона с горизонтальным делителем версии 2.0
 */
public class LineRegionXmlIOv2Test {
    
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addPack(new N2oRegionsV2IOPack())
                .addPack(new N2oWidgetsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/region/v2/testLineRegionXmlIOv2.region.xml");
    }
}
