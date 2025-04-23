package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.action.AnchorElementIOV1;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;


class N2oButtonFieldXmlIOv2Test {

    @Test
    void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ButtonFieldIOv2(), new FormElementIOV4(), new AnchorElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/control/v2/testButtonFieldReader.widget.xml");
    }
}
