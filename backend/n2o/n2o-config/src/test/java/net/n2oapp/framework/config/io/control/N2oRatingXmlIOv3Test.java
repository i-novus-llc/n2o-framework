package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.control.plain.RatingIOv3;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oRatingXmlIOv3Test {

    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new RatingIOv3(), new FormElementIOV5());
        assert tester.check("net/n2oapp/framework/config/io/control/testRatingV3.widget.xml");
    }
}
