package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.io.action.AnchorElementIOV1;
import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class ListWidgetXmlIOv4Test {
    @Test
    public void testListWidgetXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addPack(new N2oWidgetsIOPack())
                .addPack(new N2oCellsIOPack())
                .ios(new AnchorElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/widget/list/testWidgetListIOv4.widget.xml");
    }
}
