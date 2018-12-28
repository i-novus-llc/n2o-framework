package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.CheckboxCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.action.InvokeActionCompiler;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class CheckboxCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack());
        builder.ios(new CheckboxCellElementIOv2());
        builder.compilers(new CheckboxCellCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testCell.object.xml"));
    }

    @Test
    public void testCheckboxCell() {
        check("net/n2oapp/framework/config/mapping/testCheckboxCell.widget.xml",
                "components/widgets/Table/cells/CheckboxCell/CheckboxCell.meta.json")
                .exclude("id", "actionId", "action", "widgetId", "dataProvider") //todo
                .cutXml("table.cells[0]")
                .assertEquals();
    }

}
