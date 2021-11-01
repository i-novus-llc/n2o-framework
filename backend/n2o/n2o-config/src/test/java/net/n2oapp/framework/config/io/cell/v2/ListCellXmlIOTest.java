package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи ячейки со списком
 */
public class ListCellXmlIOTest {
    @Test
    public void testListCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV4())
                .addPack(new N2oCellsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v2/testListCellIOv4.widget.xml");
    }
}
