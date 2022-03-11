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
public class ScrollspyRegionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testScrollspyRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/simple/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Регион с автоматически прокручиваемым меню");

        ScrollspyRegion region = page.regions().region(0, ScrollspyRegion.class);
        region.shouldExists();
        region.menuShouldHavePosition(ScrollspyRegion.MenuPosition.right);
        ScrollspyRegion.Menu menu = region.menu();
        menu.shouldHaveTitle("Меню");
        menu.menuItem("Личные данные").shouldBeVisible();
        menu.menuItem("Дополнительная информация").shouldBeVisible();

        ScrollspyRegion.DropdownMenuItem dropdownMenuItem = menu.dropdownMenuItem(0);
        dropdownMenuItem.shouldHaveText("Вложенное меню");
        dropdownMenuItem.menuItem("Список участников").shouldBeVisible();
        dropdownMenuItem.menuItem("Список победителей").shouldBeVisible();
        dropdownMenuItem.click();
        dropdownMenuItem.menuItem("Список участников").shouldBeHidden();
        dropdownMenuItem.menuItem("Список победителей").shouldBeHidden();
        dropdownMenuItem.click();

        region.activeContentItemShouldBe("Личные данные");
        region.activeMenuItemShouldBe("Личные данные");

        menu.menuItem("Дополнительная информация").click();
        region.activeContentItemShouldBe("Дополнительная информация");
        region.activeMenuItemShouldBe("Дополнительная информация");
    }

    @Test
    public void testContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/content/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тестирование контента");

        ScrollspyRegion scrollspy = page.regions().region(0, ScrollspyRegion.class);
        scrollspy.activeMenuItemShouldBe("Элемент1");
        scrollspy.activeContentItemShouldBe("Элемент1");
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
        formWidget.fields().field("Поле1").shouldExists();

        formWidget = scrollspy.contentItem("Элемент2").content().widget(FormWidget.class);
        formWidget.shouldExists();
        formWidget.fields().field("Поле2").shouldExists();
    }

    @Test
    public void testTabsInScrollspy() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/tabs_in_scrollspy/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тестирование табов внутри scrollspy");

        ScrollspyRegion scrollspy = page.regions().region(0, ScrollspyRegion.class);
        scrollspy.activeMenuItemShouldBe("Элемент1");
        scrollspy.activeContentItemShouldBe("Элемент1");
        TabsRegion tabs = scrollspy.contentItem(0).content().region(TabsRegion.class);
        tabs.tab(0).shouldBeActive();

        tabs.tab(1).click();
        tabs.tab(1).shouldBeActive();
        scrollspy.activeMenuItemShouldBe("Элемент1");
        scrollspy.activeContentItemShouldBe("Элемент1");

        scrollspy.menu().menuItem("Элемент2").click();
        scrollspy.activeMenuItemShouldBe("Элемент2");
        scrollspy.activeContentItemShouldBe("Элемент2");
        tabs.tab(1).shouldBeActive();
    }

    @Test
    public void testScrollspyInTabs() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/scrollspy_in_tabs/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тестирование scrollspy внутри табов");

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        tabs.tab(0).shouldBeActive();
        ScrollspyRegion scrollspy1 = tabs.tab(0).content().region(ScrollspyRegion.class);
        scrollspy1.activeMenuItemShouldBe("Элемент1 в первом табе");
        scrollspy1.activeContentItemShouldBe("Элемент1 в первом табе");

        scrollspy1.menu().menuItem("Элемент2 в первом табе").click();
        scrollspy1.activeMenuItemShouldBe("Элемент2 в первом табе");
        scrollspy1.activeContentItemShouldBe("Элемент2 в первом табе");
        tabs.tab(0).shouldBeActive();

        tabs.tab(1).click();
        tabs.tab(1).shouldBeActive();
        ScrollspyRegion scrollspy2 = tabs.tab(1).content().region(ScrollspyRegion.class);
        scrollspy2.activeMenuItemShouldBe("Элемент1 во втором табе");
        scrollspy2.activeContentItemShouldBe("Элемент1 во втором табе");

        scrollspy2.menu().menuItem("Элемент3 во втором табе").click();
        scrollspy2.activeMenuItemShouldBe("Элемент3 во втором табе");
        scrollspy2.activeContentItemShouldBe("Элемент3 во втором табе");
        tabs.tab(1).shouldBeActive();

        tabs.tab(0).click();
        tabs.tab(0).shouldBeActive();
        scrollspy1.activeMenuItemShouldBe("Элемент2 в первом табе");
        scrollspy1.activeContentItemShouldBe("Элемент2 в первом табе");
    }

    @Test
    public void testDefaultActiveElement() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/active/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тестирование активного элемента по умолчанию");

        ScrollspyRegion region = page.regions().region(0, TabsRegion.class).tab(0).content().region(ScrollspyRegion.class);
        region.shouldExists();
        region.menuShouldHavePosition(ScrollspyRegion.MenuPosition.left);
        region.activeMenuItemShouldBe("Дополнительная информация");
        region.activeContentItemShouldBe("Дополнительная информация");
    }
}
