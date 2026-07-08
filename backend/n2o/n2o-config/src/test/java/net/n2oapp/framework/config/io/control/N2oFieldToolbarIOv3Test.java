package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.action.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.SubmenuIOv2;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oFieldToolbarIOv3Test {

    @Test
    void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new AnchorElementIOV2(), new SubmenuIOv2(), new InputTextIOv3(), new FormElementIOV5(),
                new ButtonIOv2(), new InvokeActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/control/testFieldToolbarIOV3.widget.xml");
    }
}
