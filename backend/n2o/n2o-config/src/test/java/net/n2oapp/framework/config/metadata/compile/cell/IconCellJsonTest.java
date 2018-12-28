package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.IconCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json для ячейки иконка
 */
public class IconCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack());
        builder.ios(new IconCellElementIOv2());
        builder.compilers(new IconCellCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testTable.query.xml"));
    }

    @Test
    public void testIconCell() {
        check("net/n2oapp/framework/config/mapping/testIconCell.widget.xml",
                "components/widgets/Table/cells/IconCell/IconCell.meta.json")
                .cutXml("table.cells[0]")
                .changeValue("icon", "`type.id == '1' ? 'fa fa-plus' : null`")
                .exclude("id", "fieldKey", "src")
               .assertEquals();
    }

    @Test
    public void testInlineIconCellI() {
        check("net/n2oapp/framework/config/mapping/testIconCell.widget.xml",
                "components/widgets/Table/cells/IconCell/IconCell.meta.json")
                .cutXml("table.cells[1]")
                .exclude("id", "fieldKey", "src")
                .assertEquals();
    }
}
