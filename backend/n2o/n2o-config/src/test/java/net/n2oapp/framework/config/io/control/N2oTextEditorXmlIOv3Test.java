package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.TextEditorIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента редактирования текста
 */
class N2oTextEditorXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TextEditorIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testTextEditorV3.widget.xml");
    }
}
