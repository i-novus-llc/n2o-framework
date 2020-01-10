package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(),
                new N2oWidgetsPack(), new N2oCellsPack(), new N2oActionsPack());
    }

    @Test
    public void testListWidget() {
        ListWidget listWidget = (ListWidget) compile("net/n2oapp/framework/config/metadata/compile/widgets/testListWidgetCompile.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml", "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new WidgetContext("testListWidgetCompile"));

        assertThat(listWidget.getId(), is("$testListWidgetCompile"));
        assertThat(listWidget.getList().get("leftTop").getSrc(), is("ImageCell"));

        assertThat(listWidget.getList().get("header").getSrc(), is("ProgressBarCell"));
        assertThat(((N2oProgressBarCell) listWidget.getList().get("header")).getColor(), is("test"));

        assertThat(listWidget.getList().get("body").getSrc(), is("BadgeCell"));
        assertThat(listWidget.getList().get("body").getJsonVisible(), is("`1 == 2`"));

        assertThat(listWidget.getList().get("subHeader").getSrc(), is("CheckboxCell"));
        assertThat(((N2oCheckboxCell) listWidget.getList().get("subHeader")).getDisabled(), is("`!(name!='Мария')`"));

        assertThat(listWidget.getList().get("rightTop").getSrc(), is("LinkCell"));
        assertThat(((N2oLinkCell) listWidget.getList().get("rightTop")).getActionId(), is("rightTopId"));

        assertThat(listWidget.getList().get("rightBottom").getSrc(), is("IconCell"));
        assertThat(listWidget.getList().get("rightBottom").getCssClass(), is("test"));

        assertThat(listWidget.getList().get("extra").getSrc(), is("ButtonsCell"));
        assertThat(((InvokeAction) listWidget.getActions().get("menuItem0")).getOperationId(), is("create"));
        assertThat(((InvokeAction) listWidget.getActions().get("menuItem0")).getObjectId(), is("utBlank"));
        assertThat(listWidget.getActions().containsKey("rightTopId"), is(true));
    }
}