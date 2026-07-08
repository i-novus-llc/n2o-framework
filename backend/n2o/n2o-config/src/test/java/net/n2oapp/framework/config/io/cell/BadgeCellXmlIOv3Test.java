package net.n2oapp.framework.config.io.cell;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.widget.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи ячейки со значком
 */
class BadgeCellXmlIOv3Test {
    
    @Test
    void testBadgeCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV5(), new CloseActionElementIOV2())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/testBadgeCellIOv3.widget.xml");
    }
}
