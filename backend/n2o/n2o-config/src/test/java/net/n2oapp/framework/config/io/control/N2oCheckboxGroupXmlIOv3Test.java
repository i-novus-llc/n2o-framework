package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.list.CheckboxGroupIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения, записи выбора чекбосков версии 3.0
 */
class N2oCheckboxGroupXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CheckboxGroupIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testCheckboxGroupV3.widget.xml");
    }
}