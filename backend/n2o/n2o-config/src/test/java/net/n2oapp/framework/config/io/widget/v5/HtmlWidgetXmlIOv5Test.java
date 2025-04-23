package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class HtmlWidgetXmlIOv5Test {

    @Test
    void testHtmlWidgetXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new HtmlWidgetElementIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/html/testHtmlWidgetIOv5.widget.xml");
    }
}
