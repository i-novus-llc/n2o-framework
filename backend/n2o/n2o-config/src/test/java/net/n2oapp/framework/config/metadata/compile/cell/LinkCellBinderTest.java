package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.cell.LinkCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование биндинга ячейки таблицы с ссылкой
 */
class LinkCellBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
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
    void testUrlResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/cell/testLinkCellBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/cell/test.object.xml");
        PageContext context = new PageContext("testLinkCellBinder", "/p/w/:id/modal");
        SimplePage page = (SimplePage) pipeline.get(context, new DataSet().add("id", "2").add("name", "test").add("age", 21));
        LinkCell linkCell = (LinkCell) ((Table) page.getWidget()).getComponent().getBody().getCells().get(0);
        assertThat(((LinkAction) linkCell.getAction()).getUrl(), is("/p/w/2/modal/:name/open"));
        assertThat(((LinkAction) linkCell.getAction()).getPathMapping().get("name").getBindLink(), is("models.resolve['p_w_modal_w1']"));
        assertThat(((LinkAction) linkCell.getAction()).getPathMapping().get("name").getValue(), is("`name`"));
        assertThat(((LinkAction) linkCell.getAction()).getQueryMapping().get("age").getBindLink(), is("models.resolve['p_w_modal_w1']"));
        assertThat(((LinkAction) linkCell.getAction()).getQueryMapping().get("age").getValue(), is("`age`"));
        assertThat(((LinkAction) linkCell.getAction()).getQueryMapping().get("orgId").getBindLink(), nullValue());
        assertThat(((LinkAction) linkCell.getAction()).getQueryMapping().get("orgId").getValue(), is(22));
    }
}
