package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента вывода изображения
 */
public class N2oImageFieldIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ImageFieldIOv3(), new FormElementIOV5(), new AnchorElementIOV2(),
                new InvokeActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testImageFieldV3.widget.xml");
    }
}
