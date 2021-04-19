package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oCellsPack()
        );
    }

    /**
     * резолв PathParameter у rowClick
     */
    @Test
    public void rowClickResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testTableRowClick.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        PageContext context = new PageContext("testTableRowClick", "/p/w/:param/row");
        TableWidgetComponent component = (TableWidgetComponent) ((SimplePage) pipeline.get(context, new DataSet().add("param", "1"))).getWidget().getComponent();
        assertThat(component.getRowClick().getUrl(), is("/p/w/1/row/p_w_row_main_row"));
        assertThat(component.getRowClick().getPathMapping().isEmpty(), is(true));
    }
}
