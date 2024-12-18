package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.config.io.action.v2.AlertActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.RefreshActionElementIOV2;
import net.n2oapp.framework.config.metadata.pack.N2oAllIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи региона `<sub-page>` версии 3.0
 */
class SubPageRegionXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addPack(new N2oAllIOPack())
                .addIO(new AlertActionElementIOV2())
                .addIO(new RefreshActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/region/v3/testSubPageRegionXmlIOv3.region.xml");
    }
}
