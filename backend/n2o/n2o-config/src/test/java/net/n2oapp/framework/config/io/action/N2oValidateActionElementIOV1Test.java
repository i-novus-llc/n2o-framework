package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <perform-validate>
 */
public class N2oValidateActionElementIOV1Test {

    @Test
    public void testN2oValidateActionElementIOV1Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv2(), new N2oValidateActionElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/action/testN2oValidateActionElementIOV1.page.xml");
    }

}