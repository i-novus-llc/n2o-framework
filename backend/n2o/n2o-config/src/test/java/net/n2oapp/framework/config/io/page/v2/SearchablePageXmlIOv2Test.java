package net.n2oapp.framework.config.io.page.v2;

import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsV1IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи страницы с поисковой строкой версии 2.0
 */
public class SearchablePageXmlIOv2Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SearchablePageElementIOv2(), new TableElementIOV4(), new ButtonIO(), new SubmenuIO())
                .addPack(new N2oRegionsV1IOPack());

        assert tester.check("net/n2oapp/framework/config/io/page/testSearchablePageIOv2.page.xml");
    }
}
