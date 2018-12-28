package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 *Тестирование чтения и записи страницы с единственным виджетом версии 2
 */
public class SimplePageXmlIOv2Test {
    @Test
    public void testSimplePageXmlIOv2Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new TableElementIOV4()).addFieldSet4Reader().addPage2())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister().addPersister(new TableElementIOV4()).addPersister(new SimplePageElementIOv2()));
        assert tester.check("net/n2oapp/framework/config/io/page/testSimplePageIOv2.page.xml");
    }
}
