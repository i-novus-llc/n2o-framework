package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.widget.v5.toolbar.ButtonXmlIOv2Test;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode.query;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class N2oActionV5AdapterTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack()).ios(new ButtonIOv2());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
    }

    @Test
    public void testPageActionV5adapterTransformer() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/transformer/testPageActionTransformer.widget.xml")
                .get(new WidgetContext("testPageActionTransformer"));
        assertThat(((LinkAction)table.getToolbar().getGroup(0).getButtons().get(0).getAction()).getUrl(), is("/test/open"));
        assertThat(((LinkAction)table.getToolbar().getGroup(0).getButtons().get(1).getAction()).getUrl(), is("/test/withoutRoute"));
        assertThat(((LinkAction)table.getToolbar().getGroup(0).getButtons().get(2).getAction()).getUrl(), is("/test/menuItem2"));
    }

    @Test
    public void testInvokeActionV5adapterTransformer() {
        SimplePage page = (SimplePage) read("net/n2oapp/framework/config/metadata/transformer/testInvokeActionTransformer.page.xml")
                .transform().compile().get(new PageContext("testInvokeActionTransformer"));
        assertThat(((InvokeAction)page.getWidget().getToolbar().getGroup(0).getButtons().get(0).getAction()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/testInvokeActionTransformer/test/delete"));
        assertThat(((InvokeAction)page.getWidget().getToolbar().getGroup(0).getButtons().get(1).getAction()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/testInvokeActionTransformer/test/menuItem1"));
    }
}
