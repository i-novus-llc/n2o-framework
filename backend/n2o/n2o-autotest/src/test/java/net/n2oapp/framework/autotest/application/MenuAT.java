package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
public class MenuAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
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
                new N2oControlsPack(), new N2oActionsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/index.page.xml"));
    }

    @Test
    public void headerMenuItemWithIconAndBadgeTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/app.application.xml"));

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
    public void headerMenuItemWithIconTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/with_text/app.application.xml"));

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
    public void headerMenuItemWithImageAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/app.application.xml"));

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
    public void headerMenuItemWithImageTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/with_text/app.application.xml"));

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
    public void headerDropdownMenuItemWithImage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/dropdown/app.application.xml"));

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
    public void sidebarMenuItemWithIconTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/icon/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.mini);
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
    public void sidebarMenuItemWithImageAndText() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.mini);
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
    public void sidebarMenuItemWithImageTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/with_badge/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.mini);
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
}
