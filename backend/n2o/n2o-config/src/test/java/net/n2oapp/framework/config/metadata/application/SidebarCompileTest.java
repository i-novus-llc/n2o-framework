package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Side;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SearchBar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oQueriesPack());
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
        assertThat(sidebar.getToggleOnHover(), is(false));
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
        ArrayList<HeaderItem> headerItems = sidebar.getMenu().getItems();
        // sub-menu
        assertThat(headerItems.get(0).getTitle(), is("test"));
        assertThat(headerItems.get(0).getHref(), is("/page1"));
        assertThat(headerItems.get(0).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(headerItems.get(0).getSubItems().size(), is(3));
        assertThat(headerItems.get(0).getType(), is("dropdown"));
        // page
        assertThat(headerItems.get(1).getTitle(), is("headerLabel"));
        assertThat(headerItems.get(1).getHref(), is("/pageWithoutLabel"));
        assertThat(headerItems.get(1).getPageId(), is("pageWithoutLabel"));
        assertThat(headerItems.get(1).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(headerItems.get(1).getSubItems(), nullValue());
        assertThat(headerItems.get(1).getType(), is("link"));
        // a
        assertThat(headerItems.get(2).getTitle(), is("hrefLabel"));
        assertThat(headerItems.get(2).getHref(), is("http://test.com"));
        assertThat(headerItems.get(2).getLinkType(), is(HeaderItem.LinkType.outer));
        assertThat(headerItems.get(2).getSubItems(), nullValue());
        assertThat(headerItems.get(2).getType(), is("link"));

        ArrayList<HeaderItem> subItems = headerItems.get(0).getSubItems();
        // sub-menu page
        assertThat(subItems.get(0).getTitle(), is("test2"));
        assertThat(subItems.get(0).getHref(), is("/page1"));
        assertThat(subItems.get(0).getPageId(), is("pageWithoutLabel"));
        assertThat(subItems.get(0).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(subItems.get(0).getSubItems(), nullValue());
        assertThat(subItems.get(0).getType(), is("link"));
        assertThat(subItems.get(0).getProperties().size(), is(1));
        assertThat(subItems.get(0).getProperties().get("testAttr"), is("testAttribute"));
        // sub-menu a
        assertThat(subItems.get(1).getTitle(), is("hrefLabel"));
        assertThat(subItems.get(1).getHref(), is("http://test.com"));
        assertThat(subItems.get(1).getLinkType(), is(HeaderItem.LinkType.outer));
        assertThat(subItems.get(1).getSubItems(), nullValue());
        assertThat(subItems.get(1).getType(), is("link"));
        // sub-menu sub-menu
        assertThat(subItems.get(2).getTitle(), is("sub1"));
        assertThat(subItems.get(2).getHref(), is("/pageWithoutLabel"));
        assertThat(subItems.get(2).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(subItems.get(2).getSubItems().size(), is(1));
        assertThat(subItems.get(2).getType(), is("dropdown"));
        // sub-menu sub-menu page
        assertThat(subItems.get(2).getSubItems().get(0).getTitle(), is("test2"));
        assertThat(subItems.get(2).getSubItems().get(0).getPageId(), is("pageWithoutLabel"));
        assertThat(subItems.get(2).getSubItems().get(0).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(subItems.get(2).getSubItems().get(0).getType(), is("link"));

        assertThat(sidebar.getExtraMenu().getItems().size(), is(1));
        HeaderItem extraItem = sidebar.getExtraMenu().getItems().get(0);
        // sub-menu
        assertThat(extraItem.getTitle(), is("#{username}"));
        assertThat(extraItem.getImage(), is("#{image}"));
        assertThat(extraItem.getHref(), is("https://ya.ru/"));
        assertThat(extraItem.getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(extraItem.getSubItems().size(), is(2));
        assertThat(extraItem.getType(), is("dropdown"));
        subItems = extraItem.getSubItems();
        // sub-menu a
        assertThat(subItems.get(0).getTitle(), is("Test"));
        assertThat(subItems.get(0).getHref(), is("https://ya.ru/"));
        assertThat(subItems.get(0).getLinkType(), is(HeaderItem.LinkType.outer));
        assertThat(subItems.get(0).getIcon(), is("test-icon"));
        assertThat(subItems.get(0).getTarget(), is(Target.newWindow));
        assertThat(subItems.get(0).getSubItems(), nullValue());
        assertThat(subItems.get(0).getType(), is("link"));
        // sub-menu sub-menu
        assertThat(subItems.get(1).getTitle(), is("sub2"));
        assertThat(subItems.get(1).getHref(), is("/pageWithoutLabel"));
        assertThat(subItems.get(1).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(subItems.get(1).getSubItems().size(), is(1));
        assertThat(subItems.get(1).getType(), is("dropdown"));
        // sub-menu sub-menu page
        assertThat(subItems.get(1).getSubItems().get(0).getTitle(), is("test2"));
        assertThat(subItems.get(1).getSubItems().get(0).getPageId(), is("pageWithoutLabel"));
        assertThat(subItems.get(1).getSubItems().get(0).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(subItems.get(1).getSubItems().get(0).getType(), is("link"));
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
    public void testBind() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/sidebarWithMenu.application.xml")
                .bind().get(new ApplicationContext("sidebarWithMenu"), null);

        assertThat(application.getSidebar().getExtraMenu().getItems().get(0).getTitle(), is("test"));
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
