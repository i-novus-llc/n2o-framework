package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json
 */
public class TableJsonTest extends JsonMetadataTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(),
                new N2oWidgetsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testTable.query.xml"));
    }

    @Test
    public void table() {
        check("net/n2oapp/framework/config/mapping/testTableJson.widget.xml",
                "components/widgets/Table/TableWidget.meta.json")
                        .cutJson("Page_Table")
                .exclude("actions",
                        "table.style", "table.autoFocus",
                        "dataProvider.queryMapping",
                        "table.cells[0].fieldKey",
                        "table.cells[1].fieldKey",
                        "table.cells[1].textPlace",
                        "table.cells[2].fieldKey",
                        "toolbar")
                .assertEquals();
    }

}
