package net.n2oapp.framework.config.io.widget;


import net.n2oapp.framework.config.io.control.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.fieldset.*;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи формы из/в xml-файла
 */
public class FormXmlIOTest {

    @Test
    public void testFormXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new RowElementIO4(), new ColElementIO4(), new FormElementIOV4(),
                new ButtonIO(), new InputTextIOv2());

        assert tester.check("net/n2oapp/framework/config/io/widget/form/testFormWidgetIOv4.widget.xml");
    }
}
