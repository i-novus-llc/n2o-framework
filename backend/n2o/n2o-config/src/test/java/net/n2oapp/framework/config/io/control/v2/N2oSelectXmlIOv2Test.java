package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.control.v2.list.SelectIOv2;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента выбора из выпадающего списка
 */
public class N2oSelectXmlIOv2Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SelectIOv2(), new FormElementIOV4());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testSelect.widget.xml");
    }
}
