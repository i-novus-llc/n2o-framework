package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class HtmlWidgetXmlIOTest extends BaseWidgetReaderTest {

    @Test
    public void testHtmlXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new HtmlWidgetElementIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/html/testIOHtmlWidget.widget.xml");
    }
}
