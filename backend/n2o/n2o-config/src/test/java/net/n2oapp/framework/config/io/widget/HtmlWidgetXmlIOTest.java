package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class HtmlWidgetXmlIOTest extends BaseWidgetReaderTest {

    @Test
    public void testFormXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader()
                        .addControlReader()
                        .addReader(new HtmlWidgetElementIOv4()))
                .addPersister(new SelectiveStandardPersister()
                        .addPersister(new HtmlWidgetElementIOv4()));

        assert tester.check("net/n2oapp/framework/config/io/widget/html/testIOHtmlWidget.widget.xml");
        assert tester.check("net/n2oapp/framework/config/io/widget/html/testIOHtmlWidget1.widget.xml");
    }
}
