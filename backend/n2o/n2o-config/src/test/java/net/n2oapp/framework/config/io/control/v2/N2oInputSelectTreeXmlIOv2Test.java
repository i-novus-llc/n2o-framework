package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.control.v2.list.InputSelectTreeIOv2;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента ввода с выбором в выпадающем списке в виде дерева
 */
class N2oInputSelectTreeXmlIOv2Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputSelectTreeIOv2(), new FormElementIOV4());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testInputSelectTree.widget.xml");
    }
}