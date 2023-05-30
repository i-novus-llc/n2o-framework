package net.n2oapp.framework.config.io.widget.v4;


import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.fieldset.v4.ColElementIO4;
import net.n2oapp.framework.config.io.fieldset.v4.RowElementIO4;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи виджета Форма
 */
public class FormXmlIOv4Test {

    @Test
    void testFormXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new RowElementIO4(), new ColElementIO4(), new FormElementIOV4(),
                new ButtonIO(), new InputTextIOv2());

        assert tester.check("net/n2oapp/framework/config/io/widget/form/testFormWidgetIOv4.widget.xml");
    }
}
