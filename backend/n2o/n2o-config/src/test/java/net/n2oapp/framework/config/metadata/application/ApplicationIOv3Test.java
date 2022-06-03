package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.config.io.action.v2.AlertActionElementIOV2;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIOv3;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи приложения версии 3.0
 */
public class ApplicationIOv3Test {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ApplicationIOv3(), new AlertActionElementIOV2(), new SidebarIOv3());
        assert tester.check("net/n2oapp/framework/config/metadata/application/applicationIOv3.application.xml");
    }

}
