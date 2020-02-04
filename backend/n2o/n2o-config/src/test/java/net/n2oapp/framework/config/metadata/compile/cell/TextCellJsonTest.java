package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.TextCellElementIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование маппинга java модели в json для ячейки с текстом
 */
public class TextCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new TextCellElementIOv2());
        builder.compilers(new TextCellCompiler());
    }

    @Test
    public void testTextCell() {
        check("net/n2oapp/framework/config/mapping/testTextCell.widget.xml",
                "components/widgets/Table/cells/TextCell/TextCell.meta.json")
                .exclude("id", "actionId", "action", "widgetId", "dataProvider")
                .cutXml("table.cells[0]")
                .assertEquals();
    }
}
