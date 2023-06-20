package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.control.v2.plain.DatePickerIOv2;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

public class N2oDatePickerXmlIOv2Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new DatePickerIOv2(), new FormElementIOV4());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testDatePicker.widget.xml");
    }
}