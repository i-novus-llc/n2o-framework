package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oEditGrid;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * @author V. Alexeev.
 */
public class N2oEditGridXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        tester.check("net/n2oapp/framework/config/io/control/testEditGridReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oEditGrid editGrid = (N2oEditGrid) fieldSet.getItems()[0];
                    assert editGrid.getObjectId().equals("test");
                    assert editGrid.getColumns().length == 2;
                    assert editGrid.getColumns()[0].getColumnFieldId().equals("test");
                    assert editGrid.getColumns()[0].getName().equals("test");
                });
    }

}
