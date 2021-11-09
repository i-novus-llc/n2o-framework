package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи переключаемой ячейки
 */
public class SwitchCellXmlIOTest {
    @Test
    public void testSwitchCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new SwitchCellElementIOv3())
        .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v3/testSwitchCellIOv3.widget.xml");
    }
}
