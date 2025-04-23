package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.config.io.action.v2.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи страницы с тремя регионами версии 4.0
 */
class TopLeftRightPageXmlIOv4Test {
    
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardDatasourceIO(), new TopLeftRightPageElementIOv4(), new CloseActionElementIOV2(), new ButtonIOv2(), new SubmenuIOv2())
                .addPack(new N2oRegionsV3IOPack())
                .addPack(new N2oWidgetsV5IOPack());

        assert tester.check("net/n2oapp/framework/config/io/page/v4/testTopLeftRightPageIOv4.page.xml");
    }
}