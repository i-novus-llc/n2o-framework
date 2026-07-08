package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.control.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.fieldset.ColElementIO5;
import net.n2oapp.framework.config.io.fieldset.RowElementIO5;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи виджета Форма
 */
class FormXmlIOv5Test {

    @Test
    void testFormXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new RowElementIO5(), new ColElementIO5(), new FormElementIOV5(),
                new ButtonIOv2(), new InputTextIOv3());

        assert tester.check("net/n2oapp/framework/config/io/widget/form/testFormWidgetIOv5.widget.xml");
    }
}
