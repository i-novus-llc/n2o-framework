package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтение и записи ячейки с тултипом и раскрывающимся текстовым списком
 */
public class TooltipListCellXmlIOv3Test {
    @Test
    public void testTooltipListCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v3/testTooltipListCellIOv3.widget.xml");
    }
}
