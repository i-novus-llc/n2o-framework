package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.BadgeCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json
 */
public class BadgeCellJsonTest extends JsonMetadataTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.ios(new BadgeCellElementIOv2());
        builder.compilers(new BadgeCellCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testTable.query.xml"));
    }

    @Test
    public void badgeCell() {
        check("net/n2oapp/framework/config/mapping/testBadgeCell.widget.xml",
                "components/widgets/Table/cells/BadgeCell/BadgeCell.meta.json")
                .cutXml("table.cells[0]")
                .exclude("id")
                .assertEquals();
    }
}
