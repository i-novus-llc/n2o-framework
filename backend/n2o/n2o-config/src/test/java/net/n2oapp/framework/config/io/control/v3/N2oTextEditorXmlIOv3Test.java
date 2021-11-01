package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.control.v3.plain.TextEditorIOv3;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV5;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи компонента редактирования текста
 */
public class N2oTextEditorXmlIOv3Test extends N2oStandardControlReaderTestBase {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TextEditorIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testTextEditorV3.widget.xml");
    }
}
