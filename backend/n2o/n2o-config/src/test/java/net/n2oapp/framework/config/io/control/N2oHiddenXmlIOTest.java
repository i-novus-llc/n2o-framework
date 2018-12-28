package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class N2oHiddenXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testHiddenXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testHiddenReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                  /*  assertCountField(fieldSet, 1);
                    N2oHidden hidden = (N2oHidden) fieldSet.getItems()[0];
                    assertStandardAttribute(hidden);
                    assert hidden.getDefaultValue().equals("test");*/
                });
    }
}
