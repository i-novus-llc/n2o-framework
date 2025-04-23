package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тест на чтение/запись элемента   <set-value>
 */
class SetValueElementIOV2Test {
    
    @Test
    void testSetValueElementIOV2Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new SetValueElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testSetValueElementIOV2.page.xml");
    }

}