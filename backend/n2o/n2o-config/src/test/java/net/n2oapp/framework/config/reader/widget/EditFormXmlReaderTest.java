package net.n2oapp.framework.config.reader.widget;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.config.reader.widget.widget3.EditFormXmlReaderV3;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения  edit-form из/в xml-файла
 */
public class EditFormXmlReaderTest extends BaseWidgetReaderTest {

    @Test
    public void testReaderEditForm3Widget() {
        N2oForm editForm = new SelectiveStandardReader()
                .addFieldSet3Reader()
                .addEventsReader()
                .addReader(new EditFormXmlReaderV3()).readByPath("net/n2oapp/framework/config/io/widget/editForm/testEditFormReaderV3.widget.xml");
        assertWidgetAttribute(editForm);
        assertStandardForm(editForm);
        assertFieldSetAttribute(editForm);
        assert editForm.getToolbars()[0].getGenerate()[0].equals(GenerateType.crud.name());
        assert editForm.getUpload().equals(UploadType.query);
        assert ((N2oButton) editForm.getToolbars()[0].getItems()[0]).getId().equals("test");
        assert ((N2oInvokeAction) ((N2oButton) editForm.getToolbars()[0].getItems()[0]).getAction()).getOperationId().equals("create");
    }
}

