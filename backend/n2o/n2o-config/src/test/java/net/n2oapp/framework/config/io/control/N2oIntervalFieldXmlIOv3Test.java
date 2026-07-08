package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.control.plain.IntervalFieldIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oIntervalFieldXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new IntervalFieldIOv3(), new FormElementIOV5(), new InputTextIOv3());
        assert tester.check("net/n2oapp/framework/config/io/control/testRangeFieldV3.widget.xml");
    }
}
