package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class N2oRadioGroupXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testRadioGroupXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testRadioGroupReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oRadioGroup radioGroup = (N2oRadioGroup) fieldSet.getItems()[0];
                    assertStandardListAttribute(radioGroup);
                });
    }

}
