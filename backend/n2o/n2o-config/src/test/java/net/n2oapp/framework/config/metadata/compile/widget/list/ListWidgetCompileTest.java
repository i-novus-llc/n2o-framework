package net.n2oapp.framework.config.metadata.compile.widget.list;

import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.cell.CheckboxCell;
import net.n2oapp.framework.api.metadata.meta.cell.LinkCell;
import net.n2oapp.framework.api.metadata.meta.cell.ProgressBarCell;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тестирование компиляции виджета Список
 */
class ListWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(),
                new N2oWidgetsPack(), new N2oCellsPack(), new N2oActionsPack());
    }

    @Test
    void testListWidget() {
        SimplePage page = (SimplePage) compile(
                "net/n2oapp/framework/config/metadata/compile/widgets/testListWidgetCompile.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new PageContext("testListWidgetCompile"));
        ListWidget listWidget = (ListWidget) page.getWidget();
        assertThat(listWidget.getId(), is("testListWidgetCompile_w1"));
        assertThat(listWidget.getList().get("leftTop").getSrc(), is("TextCell"));

        assertThat(listWidget.getList().get("header").getSrc(), is("ProgressBarCell"));
        assertThat(((ProgressBarCell) listWidget.getList().get("header")).getColor(), is("test"));

        assertThat(listWidget.getList().get("body").getSrc(), is("BadgeCell"));
        assertThat(listWidget.getList().get("body").getVisible(), is("`1 == 2`"));

        assertThat(listWidget.getList().get("subHeader").getSrc(), is("CheckboxCell"));
        assertThat(((CheckboxCell) listWidget.getList().get("subHeader")).getDisabled(), is("`!(name!='Мария')`"));

        assertThat(listWidget.getList().get("rightTop").getSrc(), is("LinkCell"));
        assertThat(((LinkAction) ((LinkCell) listWidget.getList().get("rightTop")).getAction()).getUrl().endsWith("test"),
                is(true));

        assertThat(listWidget.getList().get("rightBottom").getSrc(), is("IconCell"));
        assertThat(listWidget.getList().get("rightBottom").getElementAttributes().get("className"), is("test"));

        assertThat(listWidget.getList().get("extra").getSrc(), is("ButtonsCell"));
        AbstractButton extra = ((ToolbarCell) listWidget.getList().get("extra")).getToolbar().get(0).getButtons().get(0);
        assertThat(((InvokeAction) extra.getAction()).getOperationId(), is("create"));
        assertThat(((InvokeAction) extra.getAction()).getObjectId(), is("utBlank"));
        assertThat(listWidget.getList().get("rightTop"), notNullValue());

        assertThat(listWidget.getPaging().getNext(), is(true));
        assertThat(listWidget.getPaging().getPrev(), is(true));
        assertThat(listWidget.getPaging().getShowCount(), is(ShowCountTypeEnum.ALWAYS));
        assertThat(listWidget.getPaging().getShowLast(), is(true));
        assertThat(listWidget.getPaging().getSize(), is(5));
        assertThat(listWidget.getPaging().getSrc(), is("pagingSrc"));

        assertThat(((StandardDatasource) page.getDatasources().get(listWidget.getDatasource())).getProvider().getSize(), is(5));
    }

    @Test
    void testRowClick() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testListWidgetRowClick.page.xml")
                .get(new PageContext("testListWidgetRowClick"));
        List<RowClick> rowClicks = new ArrayList<>();
        page.getRegions().get("single").get(0).getContent().forEach(c -> rowClicks.add(((ListWidget) c).getRowClick()));

        assertThat(rowClicks.size(), is(9));
        assertThat(rowClicks.get(0), nullValue());
        assertThat(rowClicks.get(1).getEnablingCondition(), nullValue(String.class));
        assertThat(rowClicks.get(2).getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(3).getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(4).getEnablingCondition(), is("1==1"));
        assertThat(rowClicks.get(5).getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(6).getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(7).getEnablingCondition(), is("1==1"));
    }
}
