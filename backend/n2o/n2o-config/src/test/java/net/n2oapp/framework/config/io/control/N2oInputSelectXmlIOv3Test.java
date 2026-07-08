package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.list.InputSelectIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента ввода текста с выбором из выпадающего списка
 */
class N2oInputSelectXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputSelectIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testInputSelectV3.widget.xml");
    }
}