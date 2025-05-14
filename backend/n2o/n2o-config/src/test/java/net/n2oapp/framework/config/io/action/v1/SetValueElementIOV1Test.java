package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.SetValueElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тест на чтение/запись элемента   <set-value>
 */
class SetValueElementIOV1Test {
    
    @Test
    void testSetValueElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new SetValueElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testSetValueElementIOV1.page.xml");
    }

}