package net.n2oapp.framework.config.io.page.v3;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи страницы с правыми и левыми регионами версии 3.0
 */
public class LeftRightPageXmlIOv3Test {
    
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new LeftRightPageElementIOv3(), new CloseActionElementIOV1(), new ButtonIO(), new SubmenuIO())
                .addPack(new N2oRegionsV2IOPack())
                .addPack(new N2oWidgetsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/page/v3/testLeftRightPageIOv3.page.xml");
    }
}
