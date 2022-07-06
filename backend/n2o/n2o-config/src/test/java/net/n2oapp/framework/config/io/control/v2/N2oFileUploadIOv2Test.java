package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи компонента загрузки файлов
 */
public class N2oFileUploadIOv2Test {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new FileUploadIOv2(), new FormElementIOV4());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testFileUpload.widget.xml");
    }
}
