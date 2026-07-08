package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.PasswordIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oPasswordXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new PasswordIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testPasswordV3.widget.xml");
    }
}
