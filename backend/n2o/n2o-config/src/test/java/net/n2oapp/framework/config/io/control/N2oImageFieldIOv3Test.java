package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.action.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента вывода изображения
 */
class N2oImageFieldIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ImageFieldIOv3(), new FormElementIOV5(), new AnchorElementIOV2(),
                new InvokeActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/control/testImageFieldV3.widget.xml");
    }
}
