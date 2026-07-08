package net.n2oapp.framework.config.io.cell;

import net.n2oapp.framework.config.io.action.ClearActionElementIOV2;
import net.n2oapp.framework.config.io.action.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.widget.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения и записи редактируемой ячейки
 */
class EditCellXmlIOv3Test {
    
    @Test
    void testEditCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new CloseActionElementIOV2(), new InputTextIOv3(),
                new OpenPageElementIOV2(), new ClearActionElementIOV2())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/testEditCellIOv3.widget.xml");
    }
}
