package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.config.reader.page.PageXmlReaderV1;
import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.reader.widget.widget3.FormXmlReaderV3;
import net.n2oapp.framework.config.reader.widget.widget3.TableXmlReaderV3;
import net.n2oapp.framework.config.reader.widget.widget3.TreeXmlReaderV3;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Проверка чтения виджета по ref-id
 */
public class ReferenceWidgetIOTest extends BaseWidgetReaderTest {
    private SelectiveReader pageReader = new SelectiveStandardReader()
            .addControlReader()
            .addReader(new PageXmlReaderV1())
            .addReader(new FormXmlReaderV3())
            .addReader(new TableXmlReaderV3())
            .addReader(new TreeXmlReaderV3());

    @Test
    public void testRefForm() {
        N2oPage page = pageReader.readByPath("net/n2oapp/framework/config/reader/widget/testWidgetBaseReaderRefForm.page.xml");
        assertRefWidget(page);
    }
}
