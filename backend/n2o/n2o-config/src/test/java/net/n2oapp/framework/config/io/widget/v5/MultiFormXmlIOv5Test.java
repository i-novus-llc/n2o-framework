package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи виджета MultiForm
 */
class MultiFormXmlIOv5Test {

    @Test
    void testMultiFormXmlIOv5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new MultiFormElementIOv5(), new ButtonIOv2(), new InputTextIOv3()
        );

        assert tester.check("net/n2oapp/framework/config/io/widget/multiform/testMultiFormWidgetIOv5.widget.xml");
    }
}