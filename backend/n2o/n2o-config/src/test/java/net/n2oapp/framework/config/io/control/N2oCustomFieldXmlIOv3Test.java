package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oCustomFieldXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomFieldIOv3(), new FormElementIOV5(), new CustomControlIOv3());
        assert tester.check("net/n2oapp/framework/config/io/control/testCustomFieldV3.widget.xml");
    }
}