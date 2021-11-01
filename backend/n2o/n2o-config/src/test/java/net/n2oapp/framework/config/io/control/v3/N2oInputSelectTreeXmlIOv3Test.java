package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.control.v3.list.InputSelectTreeIOv3;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV5;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи компонента ввода с выбором в выпадающем списке в виде дерева
 */
public class N2oInputSelectTreeXmlIOv3Test extends N2oStandardControlReaderTestBase {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputSelectTreeIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testInputSelectTreeV3.widget.xml");
    }
}