package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента выбора числа из диапазона
 */
class N2oNumberPickerXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new NumberPickerIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testNumberPickerV3.widget.xml");
    }
}
