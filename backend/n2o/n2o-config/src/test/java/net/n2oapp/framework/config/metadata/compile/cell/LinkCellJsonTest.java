package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.LinkCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json для ячейки иконка
 */
public class LinkCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testTable.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack());
        builder.ios(new LinkCellElementIOv2());
        builder.compilers(new LinkCellCompiler());
    }

    @Test
    public void linkCellWithA() {
        check("net/n2oapp/framework/config/mapping/testLinkCell.page.xml",
                "components/widgets/Table/cells/LinkCell/LinkCell.meta.json")
                .exclude("className","style", "action", "actionId")
                .cutXml("widgets.testLinkCell_main.table.cells[0]")
               .assertEquals();
    }

    @Test
    public void linkCellWithInlineOpenPage() {
        check("net/n2oapp/framework/config/mapping/testLinkCell.page.xml",
                "components/widgets/Table/cells/LinkCell/LinkCell.meta.json")
                .exclude("className","style", "action", "actionId")
                .cutXml("widgets.testLinkCell_main.table.cells[1]")
                .assertEquals();
    }

    @Test
    public void linkCellWithInlineShowModule() {
        check("net/n2oapp/framework/config/mapping/testLinkCell.page.xml",
                "components/widgets/Table/cells/LinkCell/LinkCellWithPerform.meta.json")
                .exclude("className","style", "action", "actionId")
                .cutXml("widgets.testLinkCell_main.table.cells[2]")
                .assertEquals();
    }
    @Test
    public void linkCellWithInlineA() {
        check("net/n2oapp/framework/config/mapping/testLinkCell.page.xml",
                "components/widgets/Table/cells/LinkCell/LinkCell.meta.json")
                .exclude("className","style", "action", "actionId")
                .cutXml("widgets.testLinkCell_main.table.cells[3]")
                .assertEquals();
    }
}
