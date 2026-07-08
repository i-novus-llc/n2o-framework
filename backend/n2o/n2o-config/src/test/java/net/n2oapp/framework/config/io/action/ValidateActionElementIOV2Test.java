package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи действия <validate>
 */
class ValidateActionElementIOV2Test {

    @Test
    void testValidateActionElementIOV2() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new ValidateActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/testValidateActionElementIOV2.page.xml");
    }
}