package net.n2oapp.framework.config.io.cell;

import net.n2oapp.framework.config.io.widget.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи переключаемой ячейки
 */
class SwitchCellXmlIOv3Test {
    
    @Test
    void testSwitchCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new SwitchCellElementIOv3())
        .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/testSwitchCellIOv3.widget.xml");
    }
}
