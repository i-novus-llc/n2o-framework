package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/*
 * @author enuzhdina
 * @since 16.06.2015.
 */
public class N2oFileUploadXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testFileUploadXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testFileUploadReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oFileUpload fileUpload = (N2oFileUpload) fieldSet.getItems()[0];
                    assertStandardAttribute(fileUpload);
                    assert fileUpload.getUploadUrl().equals("test");
                });
    }
}
