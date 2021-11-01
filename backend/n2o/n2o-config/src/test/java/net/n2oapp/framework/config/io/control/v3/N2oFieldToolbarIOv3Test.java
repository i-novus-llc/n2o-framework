package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV5;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class N2oFieldToolbarIOv3Test extends N2oStandardControlReaderTestBase {

    @Test
    public void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new AnchorElementIOV2(), new SubmenuIOv2(), new InputTextIOv3(), new FormElementIOV5(),
                new ButtonIOv2(), new InvokeActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/control/v3/testFieldToolbarIOV3.widget.xml");
    }
}
