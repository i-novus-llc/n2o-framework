package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
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
public class N2oDateIntervalXmlIOTest extends N2oStandardControlReaderTestBase {
    @Test
    public void testDateIntervalXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testDateIntervalReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oDateInterval dateInterval = (N2oDateInterval) fieldSet.getItems()[0];
                    assertStandardIntervalAttribute(dateInterval);
                    assert dateInterval.getUtc().equals(true);
                    assert dateInterval.getBeginDefaultTime().equals("00:00");
                    assert dateInterval.getEndDefaultTime().equals("00:00");
                    assert dateInterval.getDateFormat().equals("DD.MM.YYYY");
                    assert dateInterval.getPopupPlacement().name().toLowerCase().equals("down_right");
                    assert dateInterval.getPopupPlacement().name().toLowerCase().equals("down_right");

                });
    }
}
