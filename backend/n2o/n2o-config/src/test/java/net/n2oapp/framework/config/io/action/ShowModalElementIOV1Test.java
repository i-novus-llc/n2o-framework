package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <show-modal>
 */
public class ShowModalElementIOV1Test {

    @Test
    public void testOpenPageElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new TableElementIOV4()).addPage2()
                                .addReader(new ShowModalElementIOV1())
                ).addPersister(new SelectiveStandardPersister().addFieldsetPersister().addPersister(new TableElementIOV4())
                        .addPersister(new StandardPageElementIOv2())
                        .addPersister(new ShowModalElementIOV1()));
        assert tester.check("net/n2oapp/framework/config/io/action/testShowModalElementIOV1.page.xml");
    }
}