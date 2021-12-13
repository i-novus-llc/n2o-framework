package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;


/**
 * Тестирование чтения/записи виджета Карточки
 */
public class CardsWidgetXmlIOV4Test {
    @Test
    public void testTilesXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CardsWidgetIOV4())
                .addPack(new N2oCellsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/cards/CardsWidgetIOV4.widget.xml");
    }
}