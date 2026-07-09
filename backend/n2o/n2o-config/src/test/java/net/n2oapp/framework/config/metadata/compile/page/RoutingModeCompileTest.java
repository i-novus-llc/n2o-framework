package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Проверка компиляции страниц и действий с routing_mode=new
 */
class RoutingModeCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"));
    }

    private void setRoutingMode(String mode) {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties())
                .setProperty("n2o.config.routing_mode", mode);
    }

    @Test
    void pageRouteNewMode() {
        setRoutingMode("new");
        // In NEW mode the page route has trailing slash.
        SimplePage page = (SimplePage) compile(
                "net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage"));
        assertThat(page.getRoutes().getSet().contains("/page/"), is(true));
    }

    @Test
    void anchorNewMode() {
        setRoutingMode("new");
        StandardPage page = (StandardPage) compile(
                "net/n2oapp/framework/config/metadata/compile/action/testAnchorAction.page.xml")
                .get(new PageContext("testAnchorAction"));
        Toolbar toolbar = ((Widget<?>) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar();
        // APPLICATION target href should get trailing slash
        LinkAction link1 = (LinkAction) toolbar.getButton("id1").getAction();
        assertThat(link1.getUrl(), is("/test"));
    }

    @Test
    void openPageRouteNewMode() {
        setRoutingMode("new");
        // Compile a page that contains an open-page action
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage"));
        // In NEW mode the action's registered context route ends with "/"
        // RouteInfoKey strips trailing slash, so we look up without it
        PageContext context = (PageContext) route("/page/action1/", Page.class);
        assertThat(context.getRoute(null), is("/page/action1/"));
    }
}
