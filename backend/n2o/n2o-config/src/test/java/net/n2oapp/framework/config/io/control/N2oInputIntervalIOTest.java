package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oInputInterval;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * @author iryabov
 * @since 21.02.2017
 */
public class N2oInputIntervalIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister().addEventPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testInputIntervalReader1.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oInputInterval field = (N2oInputInterval) fieldSet.getItems()[0];
                    assertStandardIntervalAttribute(field);
                });
    }

}