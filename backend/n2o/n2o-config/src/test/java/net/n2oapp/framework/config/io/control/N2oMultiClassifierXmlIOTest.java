package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oMultiClassifier;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;


public class N2oMultiClassifierXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testMultiClassifierXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testMultiClassifierReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oMultiClassifier multiClassifier = (N2oMultiClassifier) fieldSet.getItems()[0];
                    assertStandardListAttribute(multiClassifier);
                });
    }
}
