package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.control.v3.plain.IntervalFieldIOv3;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class N2oIntervalFieldXmlIOv3Test {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new IntervalFieldIOv3(), new FormElementIOV5(), new InputTextIOv3());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testRangeFieldV3.widget.xml");
    }
}
