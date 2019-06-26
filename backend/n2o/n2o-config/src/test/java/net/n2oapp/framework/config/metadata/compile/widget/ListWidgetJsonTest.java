package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class ListWidgetJsonTest extends JsonMetadataTestBase {

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
        check("net/n2oapp/framework/config/mapping/testListWidget.widget.xml",
                "components/widgets/List/List.meta.json")
                .cutJson("List")
                .exclude("actions", "hasMoreButton", "fetchOnScroll", "scrollY", "scrollX", "showPagination", "divider",
                        "table.style", "table.autoFocus",
                        "dataProvider.queryMapping",
                        "list.leftTop.title",
                        "list.leftTop.fieldKey",
                        "list.leftBottom.as",
                        "list.leftBottom.fieldKey",
                        "list.header.as",
                        "list.header.fieldKey",
                        "list.subHeader.as",
                        "list.subHeader.fieldKey",
                        "list.body.fieldKey",
                        "list.rightTop.fieldKey",
                        "list.rightBottom.fieldKey",
                        "list.extra.className",
                        "list.extra.style",
                        "list.extra.fieldKey",
                        "list.extra.buttons[0].size",
                        "list.extra.buttons[0].visible",
                        "list.extra.buttons[0].disabled",
                        "list.extra.buttons[0].action",
                        "list.extra.buttons[0].hintPosition",
                        "list.extra.buttons[0].conditions",
                        "list.extra.buttons[1].size",
                        "list.extra.buttons[1].visible",
                        "list.extra.buttons[1].disabled",
                        "list.extra.buttons[1].action",
                        "list.extra.buttons[1].hintPosition",
                        "list.extra.buttons[1].conditions",
                        "toolbar")
                .assertEquals();
    }
}