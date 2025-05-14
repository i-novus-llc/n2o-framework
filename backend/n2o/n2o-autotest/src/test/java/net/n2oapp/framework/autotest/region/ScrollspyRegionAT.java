package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.*;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для региона с автоматически прокручиваемым меню
 */
class ScrollspyRegionAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void testScrollspyRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/simple/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Регион с автоматически прокручиваемым меню");

        ScrollspyRegion region = page.regions().region(0, ScrollspyRegion.class);
        region.shouldExists();
        region.menuShouldHavePosition(ScrollspyRegion.MenuPositionEnum.right);
        ScrollspyRegion.Menu menu = region.menu();
        menu.menuItem("Личные данные").shouldBeVisible();
        menu.menuItem("Дополнительная информация").shouldBeVisible();

        ScrollspyRegion.DropdownMenuItem dropdownMenuItem = menu.dropdownMenuItem(0);
        dropdownMenuItem.shouldHaveText("Вложенное меню");
        dropdownMenuItem.shouldBeCollapse();
        dropdownMenuItem.click();
        dropdownMenuItem.shouldBeExpand();
        dropdownMenuItem.menuItem("Список участников").shouldBeVisible();
        dropdownMenuItem.menuItem("Список победителей").shouldBeVisible();
        dropdownMenuItem.click();
        dropdownMenuItem.menuItem("Список участников").shouldBeHidden();
        dropdownMenuItem.menuItem("Список победителей").shouldBeHidden();
        dropdownMenuItem.click();

        region.activeContentItemShouldHaveTitle("Личные данные");
        region.activeMenuItemShouldHaveTitle("Личные данные");

        menu.menuItem("Дополнительная информация").click();
        region.activeContentItemShouldHaveTitle("Дополнительная информация");
        region.activeMenuItemShouldHaveTitle("Дополнительная информация");
    }

    @Test
    void testContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/content/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование контента");

        ScrollspyRegion scrollspy = page.regions().region(0, ScrollspyRegion.class);
        scrollspy.activeMenuItemShouldHaveTitle("Элемент1");
        scrollspy.activeContentItemShouldHaveTitle("Элемент1");
        RegionItems content = scrollspy.contentItem("Элемент1").content();

        TabsRegion tabsRegion = content.region(0, TabsRegion.class);
        tabsRegion.shouldExists();
        tabsRegion.shouldHaveSize(3);

        PanelRegion panelRegion = content.region(1, PanelRegion.class);
        panelRegion.shouldExists();
        panelRegion.shouldHaveTitle("Регион в виде панелей");

        LineRegion lineRegion = content.region(2, LineRegion.class);
        lineRegion.shouldExists();
        lineRegion.shouldHaveLabel("Регион с горизонтальным делителем");

        content.region(3, SimpleRegion.class).shouldExists();
        FormWidget formWidget = content.region(3, SimpleRegion.class).content().widget(FormWidget.class);
        formWidget.shouldExists();
        formWidget.fields().field("field1").shouldExists();

        formWidget = scrollspy.contentItem("Элемент2").content().widget(FormWidget.class);
        formWidget.shouldExists();
        formWidget.fields().field("field2").shouldExists();

        scrollspy.contentItem("Элемент2").scrollDown();
        scrollspy.activeContentItemShouldHaveTitle("Элемент2");
        scrollspy.activeMenuItemShouldHaveTitle("Элемент2");
        scrollspy.contentItem("Элемент1").scrollUp();
        scrollspy.activeContentItemShouldHaveTitle("Элемент1");
        scrollspy.activeMenuItemShouldHaveTitle("Элемент1");
    }

    @Test
    void testTabsInScrollspy() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/tabs_in_scrollspy/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование табов внутри scrollspy");

        ScrollspyRegion scrollspy = page.regions().region(0, ScrollspyRegion.class);
        scrollspy.activeMenuItemShouldHaveTitle("Элемент1");
        scrollspy.activeContentItemShouldHaveTitle("Элемент1");
        TabsRegion tabs = scrollspy.contentItem(0).content().region(TabsRegion.class);
        tabs.tab(0).shouldBeActive();

        tabs.tab(1).click();
        tabs.tab(1).shouldBeActive();
        scrollspy.activeMenuItemShouldHaveTitle("Элемент1");
        scrollspy.activeContentItemShouldHaveTitle("Элемент1");

        scrollspy.menu().menuItem("Элемент2").click();
        scrollspy.activeMenuItemShouldHaveTitle("Элемент2");
        scrollspy.activeContentItemShouldHaveTitle("Элемент2");
        tabs.tab(1).shouldBeActive();
    }

    @Test
    void testScrollspyInTabs() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/scrollspy_in_tabs/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование scrollspy внутри табов");

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        tabs.tab(0).shouldBeActive();
        ScrollspyRegion scrollspy1 = tabs.tab(0).content().region(ScrollspyRegion.class);
        scrollspy1.activeMenuItemShouldHaveTitle("Элемент1 в первом табе");
        scrollspy1.activeContentItemShouldHaveTitle("Элемент1 в первом табе");

        scrollspy1.menu().menuItem("Элемент2 в первом табе").click();
        scrollspy1.activeMenuItemShouldHaveTitle("Элемент2 в первом табе");
        scrollspy1.activeContentItemShouldHaveTitle("Элемент2 в первом табе");
        tabs.tab(0).shouldBeActive();

        tabs.tab(1).click();
        tabs.tab(1).shouldBeActive();
        ScrollspyRegion scrollspy2 = tabs.tab(1).content().region(ScrollspyRegion.class);
        scrollspy2.activeMenuItemShouldHaveTitle("Элемент1 во втором табе");
        scrollspy2.activeContentItemShouldHaveTitle("Элемент1 во втором табе");

        scrollspy2.menu().menuItem("Элемент3 во втором табе").click();
        scrollspy2.activeMenuItemShouldHaveTitle("Элемент3 во втором табе");
        scrollspy2.activeContentItemShouldHaveTitle("Элемент3 во втором табе");
        tabs.tab(1).shouldBeActive();

        tabs.tab(0).click();
        tabs.tab(0).shouldBeActive();
        scrollspy1.activeMenuItemShouldHaveTitle("Элемент2 в первом табе");
        scrollspy1.activeContentItemShouldHaveTitle("Элемент2 в первом табе");
    }

    @Test
    void testDefaultActiveElement() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/active/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование активного элемента по умолчанию");

        ScrollspyRegion region = page.regions().region(0, TabsRegion.class).tab(0).content().region(ScrollspyRegion.class);
        region.shouldExists();
        region.menuShouldHavePosition(ScrollspyRegion.MenuPositionEnum.left);
        region.activeMenuItemShouldHaveTitle("Дополнительная информация");
        region.activeContentItemShouldHaveTitle("Дополнительная информация");
    }

    @Test
    void testGroup() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/group/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        ScrollspyRegion region = page.regions().region(0, ScrollspyRegion.class);
        region.shouldExists();

        region.menu().group(0).shouldHaveTitle("Первая группа");
        region.menu().group(0).shouldNotHaveHeadline();
        region.menu().group(0).menuItem(0).shouldHaveText("Личные данные");
        region.menu().group(0).menuItem(1).shouldHaveText("Дополнительная информация");
        region.menu().group(0).menuItem(1).click();
        region.activeContentItemShouldHaveTitle("Дополнительная информация");


        region.menu().group(1).shouldHaveTitle("Группа 2");
        region.menu().group(1).shouldHaveHeadline();
        region.menu().group(1).dropdownMenuItem(0).shouldBeCollapse();
        region.menu().group(1).dropdownMenuItem(0).shouldHaveText("Вложенное меню");
        region.menu().group(1).dropdownMenuItem(0).click();
        region.menu().group(1).dropdownMenuItem(0).shouldBeExpand();
        region.menu().group(1).dropdownMenuItem(0).menuItem(0).shouldHaveText("Список участников");
        region.menu().group(1).dropdownMenuItem(0).menuItem(1).shouldHaveText("Список победителей");
        region.menu().group(1).dropdownMenuItem(0).menuItem(1).click();
        region.activeContentItemShouldHaveTitle("Список победителей");
    }

}
