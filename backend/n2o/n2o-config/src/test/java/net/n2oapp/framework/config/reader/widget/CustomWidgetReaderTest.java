package net.n2oapp.framework.config.reader.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import net.n2oapp.framework.config.reader.widget.widget3.CustomWidgetXmlReaderV3;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Before;
import org.junit.Test;

/*
 * @author enuzhdina
 * @since 11.06.2015.
 */
public class CustomWidgetReaderTest extends BaseWidgetReaderTest {
    private SelectiveReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new SelectiveStandardReader()
                .addControlReader()
                .addReader(new CustomWidgetXmlReaderV3());
    }

    @Test
    public void testIOCustomWidget() {
        N2oCustomWidget customWidget = reader.readByPath("net/n2oapp/framework/config/reader/widget/custom/testCustomWidgetReader.widget.xml");

        assertWidgetAttribute(customWidget);
        assertCustomWidget(customWidget);
    }
}
