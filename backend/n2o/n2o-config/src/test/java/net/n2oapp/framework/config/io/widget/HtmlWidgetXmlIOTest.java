package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.config.persister.widget.HtmlWidgetXmlPersister;
import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.reader.widget.widget3.HtmlWidgetXmlReaderV3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class HtmlWidgetXmlIOTest extends BaseWidgetReaderTest {
    private SelectiveReader reader = new SelectiveStandardReader()
            .addControlReader()
            .addReader(new HtmlWidgetXmlReaderV3());

    private ION2oMetadataTester test = new ION2oMetadataTester()
            .addReader(reader)
            .addPersister(new HtmlWidgetXmlPersister());

    @Test
    public void testIOCustomWidget(){
        assert test.check("net/n2oapp/framework/config/io/widget/html/testIOHtmlWidget.widget.xml",
                (N2oHtmlWidget htmlWidget) -> {
                    assert htmlWidget.getUrl().equals("test.html");
                    assert htmlWidget.getObjectId().equals("blank");
                });

        assert test.check("net/n2oapp/framework/config/io/widget/html/testIOHtmlWidget1.widget.xml",
                (N2oHtmlWidget htmlWidget) -> {
                    assert htmlWidget.getUrl().equals("test.html");
                    assert htmlWidget.getName().equals("Test");
                    assert htmlWidget.getObjectId().equals("test");
                    assert htmlWidget.getProperties().size() == 1;
                    assert htmlWidget.getProperties().get("test").equals("test");
                });
    }
}
