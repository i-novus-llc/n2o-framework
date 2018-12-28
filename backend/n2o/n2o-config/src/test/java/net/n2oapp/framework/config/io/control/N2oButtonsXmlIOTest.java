package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.list.N2oCheckboxButtons;
import net.n2oapp.framework.api.metadata.control.list.N2oRadioButtons;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class N2oButtonsXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testButtonsXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testButtonsReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 2);

                    N2oCheckboxButtons checkboxButtons = (N2oCheckboxButtons) fieldSet.getItems()[0];
                    assertStandardListAttribute(checkboxButtons);
                    assert checkboxButtons.getColorFieldId().equals("test");

                    N2oRadioButtons radioButtons = (N2oRadioButtons) fieldSet.getItems()[1];
                    assertStandardListAttribute(radioButtons);
                    assert radioButtons.getColorFieldId().equals("test");
                });
    }

}
