package net.n2oapp.framework.config.io.event;

import net.n2oapp.framework.config.io.page.SimplePageElementIOv2;
import net.n2oapp.framework.config.persister.event.ValidateEventPersister;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Проверка чтения и персиста события <validate>
 */
public class N2oValidateEventIOTest {
    private ION2oMetadataTester tester;

    @Before
    public void beforeTest() {
        SelectiveReader reader = new SelectiveStandardReader()
                .addWidgetReaderV3()
                .addEventsReader()
                .addPage2();

        SelectivePersister persister = new SelectiveStandardPersister()
                .addPersister(new SimplePageElementIOv2())
                //.addPersister(new TableXmlPersister())
                .addPersister(ValidateEventPersister.getInstance());

        tester = new ION2oMetadataTester()
                .addReader(reader)
                .addPersister(persister);
    }

    @Test
    public void testIOCloseEvent() {
        //todo: напистаь тесты как появятся экшены в table
//        assert tester.check("net/n2oapp/framework/config/io/event/ioValidateEventTest.page.xml",
//                (N2oPage page) -> {
//                    N2oActionMenu menu = page.getContainers().get(0).getActionMenu();
//                    N2oActionMenu.MenuItem menuItem = (N2oActionMenu.MenuItem) menu.getMenuItemGroups()[0].getMenuItems()[0];
//                    assert menuItem.getEvent() instanceof N2OValidateAction;
//                });
    }
}
