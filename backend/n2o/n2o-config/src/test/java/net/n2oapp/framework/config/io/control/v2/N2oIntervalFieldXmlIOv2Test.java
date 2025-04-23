package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.control.v2.plain.IntervalFieldIOv2;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oIntervalFieldXmlIOv2Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new IntervalFieldIOv2(), new FormElementIOV4(), new InputTextIOv2());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testRangeField.widget.xml");
    }
}
