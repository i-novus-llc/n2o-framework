package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.DatePickerIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oDatePickerXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new DatePickerIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testDatePickerV3.widget.xml");
    }
}