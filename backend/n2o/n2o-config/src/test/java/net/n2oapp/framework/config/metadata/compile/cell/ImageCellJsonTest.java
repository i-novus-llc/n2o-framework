package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.ImageCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Тестирвоание маппинга java модели в json для ячейки картинка
 */
public class ImageCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testTable.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.ios(new ImageCellElementIOv2());
        builder.compilers(new ImageCellCompiler());
    }

    @Test
    public void imageCell() {
        check("net/n2oapp/framework/config/mapping/testImageCell.widget.xml",
                "components/widgets/Table/cells/ImageCell/ImageCell.meta.json")
                .cutXml("table.cells[0]")
                .exclude("src", "style", "title", "fieldKey")
                .assertEquals();
    }
}
