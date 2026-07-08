package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.list.SliderIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oSliderXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SliderIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testSliderV3.widget.xml");
    }}
