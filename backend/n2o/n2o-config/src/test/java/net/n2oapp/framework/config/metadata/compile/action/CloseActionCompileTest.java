package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.SubPageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CloseActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    void closeModal() {
        ModalPageContext context = new ModalPageContext("testCloseAction", "/p/w/a");
        context.setClientPageId("p_w_a");
        context.setRefreshClientDataSourceIds(Arrays.asList("p_w"));
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testCloseAction.page.xml")
                .get(context);
        CloseAction testAction = (CloseAction) page.getWidget().getToolbar().getButton("test").getAction();
        assertThat(testAction.getType(), is("n2o/overlays/CLOSE"));
        assertThat(((CloseActionPayload) testAction.getPayload()).getPageId(), is("p_w_a"));
        assertThat(((CloseActionPayload) testAction.getPayload()).getPrompt(), is(true));
        assertThat((testAction.getMeta()).getRefresh(), nullValue());
        CloseAction testCloseRefreshAction = (CloseAction) page.getWidget().getToolbar().getButton("testCloseRefresh").getAction();
        assertThat((testCloseRefreshAction.getMeta()).getRefresh().getDatasources(), hasItem("p_w"));

    }

    @Test
    void back() {
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
        assertThat(((LinkActionImpl) openPage.getWidget().getToolbar().getButton("close").getAction()).getRestore(), is(true));
    }

    @Test
    void closeSubPage() {
        PageContext context = new SubPageContext("testOpenPageCloseAction", "/p/w/a");
        context.setPageName("page1");
        N2oException e = assertThrows(N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageCloseAction.page.xml").get(context));
        assertThat(e.getMessage(), is("В странице 'page1', на которую ссылаются в регионе \"<sub-page>\", нельзя использовать действие \"<close>\""));
    }
}
