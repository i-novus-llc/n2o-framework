package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

public class N2oCustomFieldXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomFieldIOv3(), new FormElementIOV5(), new CustomControlIOv3());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testCustomFieldV3.widget.xml");
    }
}