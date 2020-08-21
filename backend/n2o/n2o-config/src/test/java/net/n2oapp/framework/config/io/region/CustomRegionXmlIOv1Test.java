package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи кастомного региона версии 1.0
 */
public class CustomRegionXmlIOv1Test {
    @Test
    public void testCustomRegionXmlIOv1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomRegionIOv1());

        assert tester.check("net/n2oapp/framework/config/io/region/testCustomRegionXmlIOv1.region.xml");
    }
}
