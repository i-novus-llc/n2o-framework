package net.n2oapp.framework.config.io.action.v1;


import net.n2oapp.framework.config.io.action.SetValueElementIOV1;
import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента   <set-value>
 */
public class SetValueElementIOV1Test {
    @Test
    public void testSetValueElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new SetValueElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testSetValueElementIOV1.page.xml");
    }

}