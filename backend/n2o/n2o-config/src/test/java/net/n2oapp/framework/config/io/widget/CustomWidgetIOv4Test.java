package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class CustomWidgetIOv4Test {
    @Test
    public void testCustomWidgetIOv4(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader()
                        .addReader(new InvokeActionElementIOV1())
                        .addReader(new CustomWidgetIOv4()))
                .addPersister(new SelectiveStandardPersister()
                        .addPersister(new CustomWidgetIOv4())
                        .addPersister(new InvokeActionElementIOV1()))
                .ios(new ButtonIO());

        assert tester.check("net/n2oapp/framework/config/io/widget/custom/testWidgetCustomIOv4.widget.xml");
    }
}
