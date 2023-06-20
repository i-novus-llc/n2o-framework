package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.config.io.action.v2.ClearActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения и записи редактируемой ячейки
 */
public class EditCellXmlIOv3Test {
    
    @Test
    void testEditCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new CloseActionElementIOV2(), new InputTextIOv3(),
                new OpenPageElementIOV2(), new ClearActionElementIOV2())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v3/testEditCellIOv3.widget.xml");
    }
}
