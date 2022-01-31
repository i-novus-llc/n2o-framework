package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.config.io.action.v2.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.ShowModalElementIOV2;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи стандартной страницы версии 4.0
 */
public class StandardPageXmlIOv4Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new ShowModalElementIOV2(), new CloseActionElementIOV2(),
                new ButtonIOv2(), new SubmenuIOv2())
                .addPack(new N2oRegionsV3IOPack())
                .addPack(new N2oWidgetsV5IOPack());

        assert tester.check("net/n2oapp/framework/config/io/page/v4/testStandardPageIOv4.page.xml");
    }
}
