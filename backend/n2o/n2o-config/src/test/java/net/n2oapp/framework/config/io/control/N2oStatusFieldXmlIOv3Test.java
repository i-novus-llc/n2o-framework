package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента отображения статуса
 */
class N2oStatusFieldXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StatusFieldIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testStatusFieldV3.widget.xml");
    }
}