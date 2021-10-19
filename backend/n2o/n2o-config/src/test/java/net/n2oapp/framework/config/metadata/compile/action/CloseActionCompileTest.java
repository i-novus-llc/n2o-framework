package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CloseActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    public void closeModal() throws Exception {
        ModalPageContext context = new ModalPageContext("testCloseAction", "/p/w/a");
        context.setClientPageId("p_w_a");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testCloseAction.page.xml")
                .get(context);
        CloseAction testAction = (CloseAction) page.getWidget().getToolbar().getButton("test").getAction();
        assertThat(testAction.getType(), is("n2o/overlays/CLOSE"));
        assertThat(((CloseActionPayload) testAction.getPayload()).getPageId(), is("p_w_a"));
        assertThat(((CloseActionPayload) testAction.getPayload()).getPrompt(), is(true));
        assertThat(( testAction.getMeta()).getRefresh(), is(nullValue()));
        CloseAction testCloseRefreshAction = (CloseAction) page.getWidget().getToolbar().getButton("testCloseRefresh").getAction();
        assertThat(( testCloseRefreshAction.getMeta()).getRefresh().getType(), is(RefreshSaga.Type.widget));

    }

    @Test
    public void back() {
        PageContext context = new PageContext("testCloseAction", "/p/w/a");
        context.setParentRoute("/p/w");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testCloseAction.page.xml")
                .get(context);
        LinkActionImpl testAction = (LinkActionImpl) page.getWidget().getToolbar().getButton("test").getAction();
        assertThat(testAction.getUrl(), is("/p/w"));

        PageContext openPageContext = (PageContext) route("/p/w/a/b/c", Page.class);
        assertThat(openPageContext.getParentRoute(), is("/p/w/a"));
        SimplePage openPage = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageCloseAction.page.xml")
                .get(openPageContext);
        assertThat(((LinkActionImpl) openPage.getWidget().getToolbar().getButton("close").getAction()).getUrl(), is("/p/w/a"));
    }
}