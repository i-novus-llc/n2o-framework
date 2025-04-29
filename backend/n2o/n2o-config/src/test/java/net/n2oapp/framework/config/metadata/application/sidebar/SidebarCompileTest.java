package net.n2oapp.framework.config.metadata.application.sidebar;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.SidebarSideEnum;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.application.SidebarStateEnum;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции бокового меню
 */
class SidebarCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
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
    void defaultSidebar() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/sidebar/defaultSidebar.application.xml")
                .get(new ApplicationContext("defaultSidebar"));
        Sidebar sidebar = application.getSidebars().get(0);
        assertThat(sidebar.getSrc(), is("Sidebar"));
        assertThat(sidebar.getDefaultState(), is(SidebarStateEnum.MAXI));
        assertThat(sidebar.getToggledState(), is(SidebarStateEnum.MINI));
        assertThat(sidebar.getToggleOnHover(), is(false));
        assertThat(sidebar.getOverlay(), is(false));
        assertThat(sidebar.getSide(), is(SidebarSideEnum.LEFT));
    }

    @Test
    void defaultSidebarSwitchedInHeader() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/sidebar/defaultSidebarSwitchedInHeader.application.xml")
                .get(new ApplicationContext("defaultSidebarSwitchedInHeader"));
        Sidebar sidebar = application.getSidebars().get(0);
        assertThat(sidebar.getSrc(), is("Sidebar"));
        assertThat(sidebar.getDefaultState(), is(SidebarStateEnum.NONE));
        assertThat(sidebar.getToggledState(), is(SidebarStateEnum.MAXI));
        assertThat(sidebar.getToggleOnHover(), is(false));
        assertThat(sidebar.getOverlay(), is(false));
        assertThat(sidebar.getSide(), is(SidebarSideEnum.LEFT));
    }

    @Test
    void sidebarMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/sidebar/sidebarWithMenu.application.xml")
                .get(new ApplicationContext("sidebarWithMenu"));

        Sidebar sidebar = application.getSidebars().get(0);
        assertThat(sidebar.getDefaultState(), is(SidebarStateEnum.NONE));
        assertThat(sidebar.getToggledState(), is(SidebarStateEnum.MICRO));
        assertThat(sidebar.getToggleOnHover(), is(true));
        assertThat(sidebar.getSide(), is(SidebarSideEnum.RIGHT));
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
    void externalMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/sidebar/sidebarWithExternalMenu.application.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/testMenu.menu.xml")
                .get(new ApplicationContext("sidebarWithExternalMenu"));

        Sidebar sidebar = application.getSidebars().get(0);
        assertThat(sidebar.getLogo().getHref(), is("http://google.com/"));
        assertThat(sidebar.getMenu().getItems().size(), is(3));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().size(), is(2));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().get(0).getTitle(), is("test2"));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().get(0).getProperties().get("testAttr"), is("testAttribute"));
        assertThat(sidebar.getMenu().getItems().get(0).getSubItems().get(0).getJsonProperties().get("testAttr"), is("testAttribute"));
        assertThat(sidebar.getMenu().getItems().get(1).getTitle(), is("headerLabel"));
    }

    @Test
    void sidebars() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/sidebar/sidebars.application.xml")
                .get(new ApplicationContext("sidebars"));

        assertThat(application.getSidebars().size(), is(2));
        Sidebar sidebar = application.getSidebars().get(0);

        assertThat(sidebar.getPath(), is("/home"));
        assertThat(sidebar.getSubtitle(), is("Home description"));

        sidebar = application.getSidebars().get(1);
        assertThat(sidebar.getPath(), is("/profile"));
    }

    @Test
    void testMenuItemsDatasource() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/sidebar/menuItemsDatasource.application.xml")
                .bind().get(new ApplicationContext("menuItemsDatasource"), null);

        Sidebar sidebar = application.getSidebars().get(0);
        //nav
        assertThat(sidebar.getDatasource(), is("test"));
        assertThat(sidebar.getMenu().getItems().size(), is(4));
        assertThat(sidebar.getMenu().getItems().get(0).getDatasource(), is("ds"));
        assertThat(sidebar.getMenu().getItems().get(0).getTitle(), is("`name`"));
        assertThat(sidebar.getMenu().getItems().get(1).getDatasource(), is("test"));

        assertThat(sidebar.getMenu().getItems().get(2).getDatasource(), is("ds"));
        assertThat(sidebar.getMenu().getItems().get(2).getSubItems().size(), is(2));
        assertThat(sidebar.getMenu().getItems().get(2).getSubItems().get(0).getDatasource(), is("test"));
        assertThat(sidebar.getMenu().getItems().get(2).getSubItems().get(0).getTitle(), is("`name2`"));
        assertThat(sidebar.getMenu().getItems().get(2).getSubItems().get(1).getDatasource(), is("ds"));

        assertThat(sidebar.getMenu().getItems().get(3).getDatasource(), is("test"));
        assertThat(sidebar.getMenu().getItems().get(3).getSubItems().size(), is(2));
        assertThat(sidebar.getMenu().getItems().get(3).getSubItems().get(0).getDatasource(), is("ds"));
        assertThat(sidebar.getMenu().getItems().get(3).getSubItems().get(1).getDatasource(), is("test"));

        //extra-menu
        assertThat(sidebar.getExtraMenu().getItems().size(), is(4));
        assertThat(sidebar.getExtraMenu().getItems().get(0).getDatasource(), is("ds"));
        assertThat(sidebar.getExtraMenu().getItems().get(0).getTitle(), is("`name`"));
        assertThat(sidebar.getExtraMenu().getItems().get(1).getDatasource(), is("test"));

        assertThat(sidebar.getExtraMenu().getItems().get(2).getDatasource(), is("ds"));
        assertThat(sidebar.getExtraMenu().getItems().get(2).getSubItems().size(), is(2));
        assertThat(sidebar.getExtraMenu().getItems().get(2).getSubItems().get(0).getDatasource(), is("test"));
        assertThat(sidebar.getExtraMenu().getItems().get(2).getSubItems().get(0).getTitle(), is("`name2`"));
        assertThat(sidebar.getExtraMenu().getItems().get(2).getSubItems().get(1).getDatasource(), is("ds"));

        assertThat(sidebar.getExtraMenu().getItems().get(3).getDatasource(), is("test"));
        assertThat(sidebar.getExtraMenu().getItems().get(3).getSubItems().size(), is(2));
        assertThat(sidebar.getExtraMenu().getItems().get(3).getSubItems().get(0).getDatasource(), is("ds"));
        assertThat(sidebar.getExtraMenu().getItems().get(3).getSubItems().get(1).getDatasource(), is("test"));
    }
}
