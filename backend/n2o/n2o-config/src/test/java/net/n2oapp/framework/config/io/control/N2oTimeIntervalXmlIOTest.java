package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oTimeInterval;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * @author V. Alexeev.
 */
public class N2oTimeIntervalXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testPasswordXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testTimeIntervalReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oTimeInterval time = (N2oTimeInterval) fieldSet.getItems()[0];
                    assertStandardIntervalAttribute(time);
                });
    }

}
