package net.n2oapp.framework.config.io.cell;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.widget.TableElementIOV5;
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

        assert tester.check("net/n2oapp/framework/config/io/cell/testCheckboxCellIOv3.widget.xml");
    }
}
