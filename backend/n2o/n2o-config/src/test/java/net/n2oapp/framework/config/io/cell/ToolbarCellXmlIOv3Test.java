package net.n2oapp.framework.config.io.cell;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.SubmenuIOv2;
import net.n2oapp.framework.config.io.widget.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи ячейки с кнопками
 */
class ToolbarCellXmlIOv3Test {
    
    @Test
    void testToolbarCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new CloseActionElementIOV2(), new ButtonIOv2(), new SubmenuIOv2())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/testToolbarCellIOv3.widget.xml");
    }
}
