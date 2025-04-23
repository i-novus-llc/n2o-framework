package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class ConfirmActionIOV2Test {

    @Test
    void testConfirmActionIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new ConfirmActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testConfirmActionIOV2.page.xml");
    }
}
