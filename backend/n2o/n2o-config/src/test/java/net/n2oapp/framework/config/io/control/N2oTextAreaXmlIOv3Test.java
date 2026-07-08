package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.TextAreaIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента ввода многострочного текста
 */
class N2oTextAreaXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TextAreaIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testTextAreaV3.widget.xml");
    }
}
