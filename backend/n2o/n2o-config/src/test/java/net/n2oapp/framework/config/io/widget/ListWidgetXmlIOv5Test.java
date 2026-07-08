package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.action.AnchorElementIOV2;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class ListWidgetXmlIOv5Test {
    
    @Test
    void testListWidgetXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addPack(new N2oWidgetsV5IOPack())
                .addPack(new N2oCellsV3IOPack())
                .ios(new AnchorElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/widget/list/testWidgetListIOv5.widget.xml");
    }
}
