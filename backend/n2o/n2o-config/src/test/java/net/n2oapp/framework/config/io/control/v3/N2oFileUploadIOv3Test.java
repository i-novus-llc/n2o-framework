package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи компонента загрузки файлов версси 3.0
 */
class N2oFileUploadIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new FileUploadIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testFileUploadV3.widget.xml");
    }
}
