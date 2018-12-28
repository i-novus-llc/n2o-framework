package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <invoke-action>
 */
public class InvokeActionElementIOV1Test {

    @Test
    public void testInvokeActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new TableElementIOV4()).addFieldSet4Reader().addPage2()
                                .addReader(new InvokeActionElementIOV1())
                ).addPersister(new SelectiveStandardPersister().addFieldsetPersister().addPersister(new TableElementIOV4())
                        .addPersister(new StandardPageElementIOv2())
                        .addPersister(new InvokeActionElementIOV1()));
        assert tester.check("net/n2oapp/framework/config/io/action/testInvokeActionElementIOV1.page.xml");
    }

}