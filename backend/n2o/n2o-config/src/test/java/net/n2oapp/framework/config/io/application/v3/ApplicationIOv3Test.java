package net.n2oapp.framework.config.io.application.v3;

import net.n2oapp.framework.config.io.action.v2.AlertActionElementIOV2;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.datasource.BrowserStorageDatasourceIO;
import net.n2oapp.framework.config.io.datasource.InheritedDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StompDatasourceIO;
import net.n2oapp.framework.config.io.event.StompEventIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи приложения версии 3.0
 */
public class ApplicationIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ApplicationIOv3(), new AlertActionElementIOV2(), new SidebarIOv3(), new StompEventIO(),
                 new StandardDatasourceIO(), new StompDatasourceIO(), new BrowserStorageDatasourceIO(), new InheritedDatasourceIO());
        assert tester.check("net/n2oapp/framework/config/io/application/v3/applicationIOv3.application.xml");
    }

}
