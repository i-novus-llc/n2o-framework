package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.config.metadata.compile.application.ApplicationIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи приложения
 */
public class ApplicationIOTest {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ApplicationIO());
        assert tester.check("net/n2oapp/framework/config/metadata/application/applicationIO.application.xml");
    }
}
