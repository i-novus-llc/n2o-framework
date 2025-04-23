package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения/записи виджета Плитки
 */
class TilesWidgetXmlIOV4Test {
    
    @Test
    void testTilesXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TilesWidgetIOV4())
                .addPack(new N2oCellsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/tiles/testTilesWidgetIOV4.widget.xml");
    }
}