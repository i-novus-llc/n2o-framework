package net.n2oapp.framework.config.io.control.masked;

import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи поля {@code <email>}
 */
class N2oEmailFieldXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new EmailFieldIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/masked/testEmailFieldV3.widget.xml");
    }
}