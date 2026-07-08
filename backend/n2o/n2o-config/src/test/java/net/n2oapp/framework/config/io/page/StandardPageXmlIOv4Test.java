package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.config.io.action.*;
import net.n2oapp.framework.config.io.datasource.*;
import net.n2oapp.framework.config.io.event.OnChangeEventIO;
import net.n2oapp.framework.config.io.event.StompEventIO;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.SubmenuIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи стандартной страницы версии 4.0
 */
class StandardPageXmlIOv4Test {
    
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardDatasourceIO(), new BrowserStorageDatasourceIO(), new ApplicationDatasourceIO(),
                new InheritedDatasourceIO(), new ParentDatasourceIO(), new CachedDatasourceIO(),
                new StandardPageElementIOv4(), new ShowModalElementIOV2(), new OnChangeEventIO(),
                new StompEventIO(), new CloseActionElementIOV2(), new ButtonIOv2(), new SubmenuIOv2(),
                new InvokeActionElementIOV2(), new AlertActionElementIOV2(), new SetValueElementIOV2())
                .addPack(new N2oRegionsV3IOPack())
                .addPack(new N2oWidgetsV5IOPack());

        assert tester.check("net/n2oapp/framework/config/io/page/testStandardPageIOv4.page.xml");
    }
}
