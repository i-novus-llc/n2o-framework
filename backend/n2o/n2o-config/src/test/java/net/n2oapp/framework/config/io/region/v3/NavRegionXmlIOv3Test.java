package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.v2.AlertActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.OpenPageElementIOV2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи региона {@code <nav>} версии 3.0
 */
class NavRegionXmlIOv3Test extends XmlIOTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(
                new NavRegionIOv3(),
                new OpenPageElementIOV2(),
                new AnchorElementIOV2(),
                new AlertActionElementIOV2()
        );
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/region/v3/testNavRegionXmlIOv3.region.xml");
    }
}
