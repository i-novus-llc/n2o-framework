package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи действия <edit-list>
 */
class EditListActionElementIOV2Test {

    @Test
    void testEditListActionElementIOV2(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new EditListActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/testEditListActionElementIOV2.page.xml");
    }
}
