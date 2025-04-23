package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class HtmlWidgetXmlIOv4Test {

    @Test
    void testHtmlWidgetXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new HtmlWidgetElementIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/html/testHtmlWidgetIOv4.widget.xml");
    }
}
