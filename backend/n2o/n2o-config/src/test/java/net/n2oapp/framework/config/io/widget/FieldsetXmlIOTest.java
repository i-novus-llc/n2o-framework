package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oPanelFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.config.io.fieldset.ColElementIO4;
import net.n2oapp.framework.config.io.fieldset.LineFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.PanelFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.RowElementIO4;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тесты чтения запист филдсета
 */
public class FieldsetXmlIOTest {

    private SelectiveReader selectiveReader = new SelectiveStandardReader()
            .addControlReader()
            .addReader(new SetFieldsetElementIOv4())
            .addReader(new LineFieldsetElementIOv4())
            .addReader(new PanelFieldsetElementIOv4())
            .addReader(new RowElementIO4())
            .addReader(new ColElementIO4());

    private SelectivePersister selectivePersister = new SelectiveStandardPersister()
            .addPersister(new SetFieldsetElementIOv4())
            .addPersister(new LineFieldsetElementIOv4())
            .addPersister(new PanelFieldsetElementIOv4())
            .addPersister(new RowElementIO4())
            .addControlPersister()
            .addPersister(new ColElementIO4());

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(selectiveReader)
            .addPersister(selectivePersister);


    @Test
    public void testIOSetFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testSetFieldsetIOv4.fieldset.xml",
                (N2oSetFieldSet fs) -> {
                });
    }

    @Test
    public void testIOLineFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testlineFieldsetIOv4.fieldset.xml",
                (N2oLineFieldSet fs) -> {
                });
    }

    @Test
    public void testIOPanelFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testPanelFieldsetIOv4.fieldset.xml",
                (N2oPanelFieldSet fs) -> {
                });
    }

}


