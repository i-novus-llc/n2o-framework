package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.config.metadata.compile.application.ApplicationIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи приложения версии 2.0
 */
public class ApplicationIOv2Test {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ApplicationIOv2());
        assert tester.check("net/n2oapp/framework/config/metadata/application/applicationIOv2.application.xml");
    }
}
