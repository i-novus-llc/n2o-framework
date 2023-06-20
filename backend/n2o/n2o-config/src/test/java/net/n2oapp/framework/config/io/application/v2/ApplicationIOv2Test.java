package net.n2oapp.framework.config.io.application.v2;

import net.n2oapp.framework.config.io.action.v2.AlertActionElementIOV2;
import net.n2oapp.framework.config.io.application.ApplicationIOv2;
import net.n2oapp.framework.config.io.datasource.BrowserStorageDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StompDatasourceIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи приложения версии 2.0
 */
public class ApplicationIOv2Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ApplicationIOv2(), new AlertActionElementIOV2(),
                new StandardDatasourceIO(), new StompDatasourceIO(), new BrowserStorageDatasourceIO());
        assert tester.check("net/n2oapp/framework/config/io/application/v2/applicationIOv2.application.xml");
    }
}
