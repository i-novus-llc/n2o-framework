package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.ToolbarCellElementIOv2;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class ToolbarCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack());
        builder.ios(new ToolbarCellElementIOv2());
        builder.compilers(new ToolbarCellCompiler());
    }

    @Test
    public void testToolbarCell() {
        check("net/n2oapp/framework/config/mapping/testToolbarCellJson.widget.xml",
                "components/widgets/Table/cells/ButtonsCell/ButtonsCell.meta.json")
                .cutXml("table.cells[0]")
                .exclude("toolbar[0].id", "className", "style", "toolbar[0].buttons[0].label", "actions.testBut.options",
                        "toolbar[0].buttons[0].size", "toolbar[0].buttons[0].color",
                        "toolbar[0].buttons[0].disabled", "actions.testBut.id",
                        "toolbar[0].buttons[0].confirm", "toolbar[0].buttons[0].conditions", "toolbar[0].buttons[0].title",
                        "toolbar[0].buttons[0].hintPosition")
                .assertEquals();
    }
}
