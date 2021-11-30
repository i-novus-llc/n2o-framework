package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
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
import org.junit.jupiter.api.Disabled;
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
                new N2oControlsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/index.page.xml"));
    }

    @Test
    public void headerMenuItemWithIconAndBadgeTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        page.header().brandNameShouldBe("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveCssClass("fa fa-bell");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("2");
        menuItem.urlShouldHave(getBaseUrl() + "/#/test");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }

    @Test
    public void headerMenuItemWithIconTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/with_text/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        page.header().brandNameShouldBe("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveCssClass("fa fa-user");
        menuItem.labelShouldHave("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("3");
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }

    @Disabled //todo убрать аннотацию после решения NNO-7186
    @Test
    public void headerMenuItemWithImageAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        page.header().brandNameShouldBe("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ImageShape.circle);
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("3");
        menuItem.urlShouldHave(getBaseUrl() + "/#/profile");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница профиля");
    }

    @Test
    public void headerMenuItemWithImageTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/image/with_text/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        page.header().brandNameShouldBe("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ImageShape.circle);
        menuItem.labelShouldHave("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("2");
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }

    @Test
    public void headerDropdownMenuItemWithImage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/dropdown/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        page.header().brandNameShouldBe("Хедер");
        page.header().nav().shouldHaveSize(1);

        DropdownMenuItem dropdown = page.header().nav().dropdown(0);
        dropdown.labelShouldHave("Виктория");
        dropdown.shouldHaveImage();
        dropdown.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        dropdown.imageShouldHaveShape(ImageShape.circle);
        dropdown.click();

        AnchorMenuItem menuItem = dropdown.item(0);
        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveCssClass("fa fa-bell");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("2");
        menuItem.urlShouldHave(getBaseUrl() + "/#/notif");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }

    @Test
    public void sidebarMenuItemWithIconTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/icon/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.mini);
        sidebar.nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.sidebar().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveCssClass("fa fa-user");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("3");
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");
        sidebar.clickToggleBtn();

        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveCssClass("fa fa-user");
        menuItem.labelShouldHave("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("3");
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }

    @Test
    public void sidebarMenuItemWithImageAndText() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.mini);
        sidebar.nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.sidebar().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ImageShape.rounded);
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");

        sidebar.clickToggleBtn();
        menuItem.shouldHaveImage();
        menuItem.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ImageShape.rounded);
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");
        menuItem.labelShouldHave("Профиль");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }

    @Test
    public void sidebarMenuItemWithImageTextAndBadge() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/sidebar/image/with_badge/app.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldHaveState(SidebarState.mini);
        sidebar.nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.sidebar().nav().anchor(0);
        menuItem.shouldHaveImage();
        menuItem.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ImageShape.circle);
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("3");
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");
        sidebar.clickToggleBtn();

        menuItem.shouldHaveImage();
        menuItem.imageSrcShouldBe(getBaseUrl() + "/images/candidate2.jpg");
        menuItem.imageShouldHaveShape(ImageShape.circle);
        menuItem.labelShouldHave("Профиль");
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("3");
        menuItem.urlShouldHave(getBaseUrl() + "/#/testMenu");

        menuItem.click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест");
    }
}
