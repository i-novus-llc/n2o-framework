package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.action.AnchorElementIOV1;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class N2oFieldToolbarIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new AnchorElementIOV1(), new SubmenuIO(), new InputTextIOv2(), new FormElementIOV4(), new ButtonIO(), new InvokeActionElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/control/v2/testFieldToolbarIO.widget.xml");
    }
}
