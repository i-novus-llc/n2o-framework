package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CloseActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new CloseActionElementIOV1());
        builder.compilers(new CloseActionCompiler(), new AnchorCompiler());
    }

    @Test
    public void closeModal() throws Exception {
        ModalPageContext context = new ModalPageContext("testCloseAction", "/p/w/a");
        context.setClientPageId("p_w_a");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testCloseAction.page.xml")
                .get(context);
        CloseAction testAction = (CloseAction) page.getWidget().getActions().get("test");
        assertThat(testAction.getType(), is("n2o/modals/CLOSE"));
        assertThat(((CloseActionPayload) testAction.getPayload()).getPageId(), is("p_w_a"));
        assertThat(((CloseActionPayload) testAction.getPayload()).getPrompt(), is(true));

    }

    @Test
    public void back() {
        PageContext context = new PageContext("testCloseAction", "/p/w/a");
        context.setParentRoute("/p/w");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testCloseAction.page.xml")
                .get(context);
        LinkActionImpl testAction = (LinkActionImpl) page.getWidget().getActions().get("test");
        assertThat(testAction.getUrl(), is("/p/w"));
    }
}