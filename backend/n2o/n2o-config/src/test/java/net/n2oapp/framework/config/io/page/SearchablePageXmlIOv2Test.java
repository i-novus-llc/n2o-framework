package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsV1IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения и записи страницы с полем поиска
 */
public class SearchablePageXmlIOv2Test {
    @Test
    public void testSearchablePageXmlIOv2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader()
                        .addReader(new TableElementIOV4())
                        .addFieldSet4Reader().addPage2()
                        .addReader(new CloseActionElementIOV1()))
                .addPersister(new SelectiveStandardPersister()
                        .addFieldsetPersister()
                        .addPersister(new TableElementIOV4())
                        .addPersister(new SearchablePageElementIOv2())
                        .addPersister(new CloseActionElementIOV1()))
                .addPack(new N2oRegionsV1IOPack())
                .ios(new ButtonIO(), new SubmenuIO());

        assert tester.check("net/n2oapp/framework/config/io/page/testSearchablePageIOv2.page.xml");
    }
}
