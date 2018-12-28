package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.persister.event.AnchorPersister;
import net.n2oapp.framework.config.persister.event.InvokeActionPersister;
import net.n2oapp.framework.config.reader.event.*;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import org.junit.Test;

/**
 * Created by schirkova on 22.05.2015.
 */
public class ActionMenuItemGroupsIOTest {
    @Test
    public void test() throws Exception {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addReader(new SelectiveReader().addReader(new TableElementIOV4()));
        tester.addPersister(new SelectivePersister().addPersister(new TableElementIOV4()));
        tester.addReader(InvokeActionReaderV1.getInstance()).addReader(new AnchorReaderV1())
                .addReader(new OpenPageReaderV1()).addReader(new ShowModalFormReaderV1()).addReader(new ShowModalWithActionReaderV1());
        tester.addPersister(AnchorPersister.getInstance()).addPersister(new InvokeActionPersister());

        assert tester.check("net/n2oapp/framework/config/selective/duplex/actionMenu.xml");
    }

}
