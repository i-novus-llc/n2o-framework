package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.config.io.action.v2.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи ячейки с ссылкой
 */
class CheckboxCellXmlIOv3Test {
    
    @Test
    void testCheckboxCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new CloseActionElementIOV2(), new OpenPageElementIOV2())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v3/testCheckboxCellIOv3.widget.xml");
    }
}
