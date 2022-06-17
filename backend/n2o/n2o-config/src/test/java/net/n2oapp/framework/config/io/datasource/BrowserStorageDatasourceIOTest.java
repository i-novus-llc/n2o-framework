package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.config.metadata.pack.N2oPagesIOv4Pack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи BrowserStorage datasource
 */
public class BrowserStorageDatasourceIOTest {
    @Test
    public void io() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new BrowserStorageDatasourceIO());
        tester.addPack(new N2oPagesIOv4Pack());
        tester.check("net/n2oapp/framework/config/io/datasource/N2oBrowserStorageDatasourceIOTest.page.xml");
    }
}
