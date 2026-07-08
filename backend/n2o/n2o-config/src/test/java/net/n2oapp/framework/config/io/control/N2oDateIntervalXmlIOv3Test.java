package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.interval.DateIntervalIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oDateIntervalXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new DateIntervalIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testDateIntervalV3.widget.xml");
    }
}