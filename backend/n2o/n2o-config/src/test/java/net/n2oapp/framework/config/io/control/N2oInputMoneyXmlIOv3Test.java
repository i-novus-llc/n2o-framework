package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.InputMoneyIOV3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oInputMoneyXmlIOv3Test {
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputMoneyIOV3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testInputMoneyV3.widget.xml");
    }
}
