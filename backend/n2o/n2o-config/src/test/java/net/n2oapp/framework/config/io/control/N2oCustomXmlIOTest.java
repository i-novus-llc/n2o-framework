package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class N2oCustomXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testCustomXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testCustomReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oCustomField customField = (N2oCustomField) fieldSet.getItems()[0];
                    assertStandardAttribute(customField);
                    assert customField.getSrc().equals("test");
                    assert customField.getDefaultValue().equals("test");
                });
    }
}
