package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.widget.CustomWidgetIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class CustomWidgetIOv5Test {
    @Test
    public void testCustomWidgetIOv4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ButtonIO(), new CustomWidgetIOv4(), new InvokeActionElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/widget/custom/testWidgetCustomIOv5.widget.xml");
    }
}
