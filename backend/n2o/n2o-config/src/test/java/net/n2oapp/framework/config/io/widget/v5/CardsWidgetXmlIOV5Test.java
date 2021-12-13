package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;


/**
 * Тестирование чтения/записи виджета Карточки версии 5.0
 */
public class CardsWidgetXmlIOV5Test {
    @Test
    public void testTilesXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CardsWidgetIOV5())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/cards/CardsWidgetIOV5.widget.xml");
    }
}