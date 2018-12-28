package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oMaskedInput;
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
public class N2oMaskedInputXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testMaskedInputXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testMaskedInputReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oMaskedInput maskedInput = (N2oMaskedInput) fieldSet.getItems()[0];
                    assertStandardAttribute(maskedInput);
                    assert maskedInput.getMask().equals("999");
                    assert maskedInput.getDefaultValue().equals("test");
                });
    }
}
