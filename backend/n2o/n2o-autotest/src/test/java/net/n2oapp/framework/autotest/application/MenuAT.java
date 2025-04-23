package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест меню
 */
class MenuAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack(), new N2oAllDataPack());
    }

    @Test
    void headerMiIconAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        page.header().shouldHaveBrandName("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-bell");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("2");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/test");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void headerMiIconTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon_with_text/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon_with_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon_with_text/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        page.header().shouldHaveBrandName("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-user");
        menuItem.shouldHaveLabel("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("3");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/notif");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void headerMiImageAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        page.header().shouldHaveBrandName("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ShapeType.CIRCLE);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("3");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/profile");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void headerMiImageTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image_with_text/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image_with_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image_with_text/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        page.header().shouldHaveBrandName("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ShapeType.CIRCLE);
        menuItem.shouldHaveLabel("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("2");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/notif");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void headerDropdownImage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/dropdown/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/dropdown/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/dropdown/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        page.header().shouldHaveBrandName("Хедер");
        page.header().nav().shouldHaveSize(1);

        DropdownMenuItem dropdown = page.header().nav().dropdown(0);
        dropdown.shouldHaveLabel("Виктория");
        dropdown.shouldHaveImage();
        dropdown.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        dropdown.imageShouldHaveShape(ShapeType.CIRCLE);
        dropdown.click();

        AnchorMenuItem menuItem = dropdown.item(0);
        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-bell");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("2");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/notif");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void sidebarMiIconTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/icon/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/icon/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/icon/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.MINI);
        sidebar.nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.sidebar().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-user");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("3");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/notif");
        sidebar.clickToggleBtn();

        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-user");
        menuItem.shouldHaveLabel("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("3");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/notif");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void sidebarMiImageAndText() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.MINI);
        sidebar.nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.sidebar().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ShapeType.ROUNDED);
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/profile");

        sidebar.clickToggleBtn();
        menuItem.shouldHaveImage();
        menuItem.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ShapeType.ROUNDED);
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/profile");
        menuItem.shouldHaveLabel("Профиль");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void sidebarMiImageTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image_with_badge/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image_with_badge/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image_with_badge/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.MINI);
        sidebar.nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.sidebar().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ShapeType.SQUARE);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("3");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/profile");
        sidebar.clickToggleBtn();

        menuItem.shouldHaveImage();
        menuItem.imageShouldHaveSrc(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ShapeType.SQUARE);
        menuItem.shouldHaveLabel("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("3");
        menuItem.shouldHaveUrl(getBaseUrl() + "/#/profile");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест");
    }

    @Test
    void differentActionInMenuItem() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/menu_item/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/menu_item/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/menu_item/page1.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        Menu menu = page.header().nav();
        AnchorMenuItem menuItem = menu.anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-bell");
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("2");

        menuItem.click();
        page.shouldExists();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт");

        menu.anchor(1).click();
        N2oSelenide.modal().shouldHaveTitle("Модальное окно");
    }

    @Test
    void nonActionMenuItem() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/non_action_menu_item/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/non_action_menu_item/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/non_action_menu_item/page1.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        Menu menu = page.header().nav();
        menu.anchor(0).shouldHaveLabel("test");
        menu.anchor(0).shouldNotBeClickable();
        menu.anchor(2).shouldHaveLabel("admin");
        menu.anchor(2).shouldNotBeClickable();
        menu.item(3, DropdownMenuItem.class).click();
        menu.item(3, DropdownMenuItem.class).item(1).shouldHaveLabel("test in dropdown");
        menu.item(3, DropdownMenuItem.class).item(1).shouldNotBeClickable();
        menu.item(3, DropdownMenuItem.class).item(2).click();
        menu.item(3, DropdownMenuItem.class).item(2, DropdownMenuItem.class).item(0).shouldHaveLabel("test in dropdown 2");
        menu.item(3, DropdownMenuItem.class).item(1).shouldNotBeClickable();

        menu = page.header().extra();
        menu.anchor(0).shouldHaveLabel("fullname");
        menu.anchor(0).shouldNotBeClickable();
    }

    @Test
    void resolveName() {
        setResourcePath("net/n2oapp/framework/autotest/application/menu/header/resolve_name/");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/resolve_name/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/resolve_name/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/resolve_name/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/resolve_name/test2.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        Sidebar sidebar = page.sidebar();

        header.shouldHaveBrandName("Хедер");

        Menu menu = page.header().nav();
        testMenu(menu);

        menu = header.extra();
        testMenu(menu);

        menu = sidebar.nav();
        testMenu(menu);

        menu = sidebar.extra();
        testMenu(menu);
    }

    private void testMenu(Menu menu) {
        menu.anchor(0).shouldHaveLabel("test1");
        menu.anchor(1).shouldHaveLabel("test2");
        menu.dropdown(2).shouldHaveLabel("type1");
        menu.dropdown(2).click();
        menu.dropdown(2).item(0).shouldHaveLabel("test1");
        menu.dropdown(2).item(1).shouldHaveLabel("test2");
        menu.dropdown(3).shouldHaveLabel("type2");
        menu.dropdown(3).click();
        menu.dropdown(3).item(0).shouldHaveLabel("test2");
        menu.dropdown(3).item(1).shouldHaveLabel("test1");
    }
}
