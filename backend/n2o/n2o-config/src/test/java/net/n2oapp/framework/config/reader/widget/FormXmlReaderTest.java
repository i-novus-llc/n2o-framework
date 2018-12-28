package net.n2oapp.framework.config.reader.widget;


import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.config.reader.widget.widget3.FormXmlReaderV3;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;


/**
 * Тестирование чтения таблицы версии ниже 4
 */
public class FormXmlReaderTest extends BaseWidgetReaderTest {

    @Test
    public void testReaderForm3Widget() {
      N2oForm form1 = new SelectiveStandardReader()
        .addFieldSet3Reader()
                .addEventsReader()
                .addReader(new FormXmlReaderV3()).readByPath("net/n2oapp/framework/config/reader/widget/form/test1FormReaderV3.widget.xml");

        N2oForm form2 = new SelectiveStandardReader()
                .addFieldSet3Reader()
                .addEventsReader()
                .addReader(new FormXmlReaderV3()).readByPath("net/n2oapp/framework/config/reader/widget/form/test2FormReaderV3.widget.xml");

        N2oForm form3 = new SelectiveStandardReader()
                .addFieldSet3Reader()
                .addEventsReader()
                .addReader(new FormXmlReaderV3()).readByPath("net/n2oapp/framework/config/reader/widget/form/test3FormReaderV3.widget.xml");

        assertWidgetAttribute(form1);
        assertStandardForm(form1);
        assertFieldSetAttribute(form1);
        assert ((N2oFieldSet)form1.getItems()[0]).getCssClass().equals("test");
        assert ((N2oFieldSet)form1.getItems()[0]).getStyle().equals("test");

        assert form1.getToolbars()[0].getGenerate()[0].equals(GenerateType.crud.name());

        assert ((N2oButton)form1.getToolbars()[0].getItems()[0]).getId().equals("test");
        assert ((N2oButton) form1.getToolbars()[0].getItems()[0]).getAction().getOperationId().equals("create");
    }
}

