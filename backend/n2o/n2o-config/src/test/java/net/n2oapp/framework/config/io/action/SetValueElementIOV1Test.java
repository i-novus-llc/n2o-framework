package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
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
        assert tester.check("net/n2oapp/framework/config/io/action/testSetValueElementIOV1.page.xml");
    }

}