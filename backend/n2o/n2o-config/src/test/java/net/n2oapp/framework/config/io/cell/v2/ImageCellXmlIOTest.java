package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.widget.v4.TableElementIOV4;
import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи ячейки с изображением
 */
public class ImageCellXmlIOTest {
    @Test
    public void testImageCell() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TableElementIOV4(), new CloseActionElementIOV1())
                .addPack(new N2oCellsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/cell/v2/testImageCellIOv4.widget.xml");
    }
}
