package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи действия <edit-list>
 */
public class EditListActionElementIOV2Test {

    @Test
    public void testEditListActionElementIOV2(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new EditListActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testEditListActionElementIOV2.page.xml");
    }
}
