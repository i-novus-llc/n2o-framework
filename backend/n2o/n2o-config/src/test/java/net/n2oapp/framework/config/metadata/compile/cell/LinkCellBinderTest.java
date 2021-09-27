package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование биндинга ячейки таблицы с ссылкой
 */
public class LinkCellBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    public void testUrlResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/cell/testLinkCellBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/cell/test.object.xml");
        PageContext context = new PageContext("testLinkCellBinder", "/p/w/:id/modal");
        SimplePage page = (SimplePage) pipeline.get(context, new DataSet().add("id", "2").add("name", "test").add("age", 21));
        N2oLinkCell linkCell = (N2oLinkCell) ((Table) page.getWidget()).getComponent().getCells().get(0);
        assertThat(linkCell.getUrl(), is("/p/w/2/modal/:name/open"));
        assertThat(linkCell.getPathMapping().get("name").getBindLink(), is("models.resolve['p_w_modal_main']"));
        assertThat(linkCell.getPathMapping().get("name").getValue(), is("`name`"));
        assertThat(linkCell.getQueryMapping().get("age").getBindLink(), is("models.resolve['p_w_modal_main']"));
        assertThat(linkCell.getQueryMapping().get("age").getValue(), is("`age`"));
        assertThat(linkCell.getQueryMapping().get("orgId").getBindLink(), nullValue());
        assertThat(linkCell.getQueryMapping().get("orgId").getValue(), is(22));
    }
}
