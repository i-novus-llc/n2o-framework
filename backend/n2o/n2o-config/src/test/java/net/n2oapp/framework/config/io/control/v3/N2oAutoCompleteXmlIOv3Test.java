package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.control.v3.plain.AutoCompleteIOv3;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения/записи компонента ввода текста с автоподбором версии 3.0
 */
class N2oAutoCompleteXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new AutoCompleteIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testAutoCompleteV3.widget.xml");
    }
}