package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения/записи виджета Плитки
 */
class TilesWidgetXmlIOV5Test {
    
    @Test
    void testTilesXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TilesWidgetIOV5())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/tiles/testTilesWidgetIOV5.widget.xml");
    }
}