package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Side;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции бокового меню
 */
public class SidebarCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        builder.getEnvironment().getContextProcessor().set("username", "test");
        builder.properties("n2o.homepage.id=index");
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oQueriesPack(), new N2oActionsPack());
    }

    @Test
    public void defaultSidebar() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/defaultSidebar.application.xml")
                .get(new ApplicationContext("defaultSidebar"));
        Sidebar sidebar = application.getSidebar();
        assertThat(sidebar.getSrc(), is("Sidebar"));
        assertThat(sidebar.getDefaultState(), is(SidebarState.none));
        assertThat(sidebar.getToggledState(), is(SidebarState.maxi));
        assertThat(sidebar.getToggleOnHover(), is(false));
        assertThat(sidebar.getOverlay(), is(false));
        assertThat(sidebar.getSide(), is(Side.left));
    }

    @Test
    public void sidebarMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/sidebarWithMenu.application.xml")
                .get(new ApplicationContext("sidebarWithMenu"));

        Sidebar sidebar = application.getSidebar();
        assertThat(sidebar.getDefaultState(), is(SidebarState.none));
        assertThat(sidebar.getToggledState(), is(SidebarState.micro));
        assertThat(sidebar.getToggleOnHover(), is(true));
        assertThat(sidebar.getSide(), is(Side.right));
        assertThat(sidebar.getOverlay(), is(false));
        assertThat(sidebar.getLogo().getSrc(), is("/logo.png"));
        assertThat(sidebar.getLogo().getClassName(), is("logo-class"));
        assertThat(sidebar.getLogo().getTitle(), is("N2O"));
        assertThat(sidebar.getSrc(), is("test"));
        assertThat(sidebar.getClassName(), is("class"));
        assertThat(sidebar.getLogo().getHref(), is("/pageRoute"));
        assertThat(sidebar.getStyle().size(), is(1));
        assertThat(sidebar.getStyle().get("marginLeft"),is("10px"));

        assertThat(sidebar.getMenu().getItems().size(), is(3));
        assertThat(sidebar.getExtraMenu().getItems().size(), is(1));
    }

    @Test
    public void externalMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/sidebarWithExternalMenu.application.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/testMenu.menu.xml")
                .get(new ApplicationContext("sidebarWithExternalMenu"));

        Sidebar sidebar = application.getSidebar();
        assertThat(sidebar.getLogo().getHref(), is("http://google.com/"));
        assertThat(sidebar.getMenu().getItems().size(), is(3));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().size(), is(2));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().get(0).getTitle(), is("test2"));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().get(0).getProperties().get("testAttr"), is("testAttribute"));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().get(0).getJsonProperties().get("testAttr"), is("testAttribute"));
        assertThat(sidebar.getMenu().getItems().get(1).getTitle(), is("headerLabel"));
    }


    @Test
    public void testInvisibleSidebar() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/invisibleSidebar.application.xml")
                .bind().get(new ApplicationContext("invisibleSidebar"), null);

        assertThat(application.getSidebar(), nullValue());
    }
}
