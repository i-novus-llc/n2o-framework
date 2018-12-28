package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента   <set-value>
 */
public class SetValueElementIOV1Test {
    @Test
    public void testSetValueElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new TableElementIOV4()).addPage2()
                                .addReader(new SetValueElementIOV1())
                ).addPersister(new SelectiveStandardPersister().addFieldsetPersister().addPersister(new TableElementIOV4())
                        .addPersister(new StandardPageElementIOv2())
                        .addPersister(new SetValueElementIOV1()));
        assert tester.check("net/n2oapp/framework/config/io/action/testSetValueElementIOV1.page.xml");
    }

}