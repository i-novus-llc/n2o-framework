package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.control.v2.list.SliderIOv2;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oSliderXmlIOv2Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SliderIOv2(), new FormElementIOV4());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testSlider.widget.xml");
    }}
