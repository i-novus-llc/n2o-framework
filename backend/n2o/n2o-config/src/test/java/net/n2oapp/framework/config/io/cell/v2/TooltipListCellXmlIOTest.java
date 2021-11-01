package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.config.io.widget.v4.TableElementIOV4;
import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтение и записи ячейки с тултипом и раскрывающимся текстовым списком
 */
public class TooltipListCellXmlIOTest {
    @Test
    public void testTooltipListCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV4())
                .addPack(new N2oCellsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v2/testTooltipListCellIOv4.widget.xml");
    }
}
