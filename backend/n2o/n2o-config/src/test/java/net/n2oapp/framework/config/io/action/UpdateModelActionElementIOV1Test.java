package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <execute-query>
 */
public class UpdateModelActionElementIOV1Test {
    @Test
    public void testCloseActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new TableElementIOV4()).addPage2()
                                .addReader(new UpdateModelActionElementIOV1())
                ).addPersister(new SelectiveStandardPersister().addFieldsetPersister().addPersister(new TableElementIOV4())
                        .addPersister(new StandardPageElementIOv2())
                        .addPersister(new UpdateModelActionElementIOV1()));
        assert tester.check("net/n2oapp/framework/config/io/action/testUpdateModelActionElementIOV1.page.xml");
    }

}