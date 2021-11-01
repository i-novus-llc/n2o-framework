package net.n2oapp.framework.config.io.widget.v5;


import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.fieldset.v5.ColElementIO5;
import net.n2oapp.framework.config.io.fieldset.v5.RowElementIO5;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи виджета Форма
 */
public class FormXmlIOv5Test {

    @Test
    public void testFormXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new RowElementIO5(), new ColElementIO5(), new FormElementIOV5(),
                new ButtonIOv2(), new InputTextIOv3());

        assert tester.check("net/n2oapp/framework/config/io/widget/form/testFormWidgetIOv5.widget.xml");
    }
}
