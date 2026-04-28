package net.n2oapp.framework.autotest.region;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.NavRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.impl.component.region.N2oNavRegion;
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
 * Автотест региона <nav>
 */
class NavRegionAT extends AutoTestBase {
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
    void testTabs() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/nav/tabs/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/nav/tabs/test1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/nav/tabs/test2.page.xml")
                );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        NavRegion nav = page.regions().region(0, NavRegion.class);
        nav.shouldExists();
        nav.content().shouldHaveSize(2);
        N2oNavRegion.N2oAnchorItem item0 = nav.content().item(0, N2oNavRegion.N2oAnchorItem.class);
        item0.shouldHaveLabel("Страница 1");
        item0.shouldBeActive();
        N2oNavRegion.N2oAnchorItem item1 = nav.content().item(1, N2oNavRegion.N2oAnchorItem.class);
        item1.shouldHaveLabel("Страница 2");
        item1.shouldNotBeActive();
        item1.click();
        item1.shouldBeActive();
        item0.shouldNotBeActive();
    }

    @Test
    void testColumn() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/nav/column/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        NavRegion nav = page.regions().region(0, NavRegion.class);
        nav.shouldExists();
        checkNavRegion(nav, page);
    }

    @Test
    void testRow() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/nav/row/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        NavRegion nav = page.regions().region(0, NavRegion.class);
        nav.shouldExists();
        checkNavRegion(nav, page);
    }

    private void checkNavRegion(NavRegion nav, StandardPage page) {
        nav.content().shouldHaveSize(6);

        // <menu-item label="Главная">
        NavRegion.NavRegionItem menuItem = nav.content().item(0, NavRegion.NavRegionItem.class);
        menuItem.shouldHaveLabel("Главная");
        menuItem.click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText("home");
        alert.closeButton();

        // <link label="Новости" href="/news"/>
        NavRegion.AnchorItem anchorItem = nav.content().item(1, NavRegion.AnchorItem.class);
        anchorItem.shouldHaveLabel("Новости");
        anchorItem.shouldHaveUrl(getBaseUrl() + "/news");
        anchorItem.click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/news");
        Selenide.back();

        // <button label="Поиск">
        NavRegion.NavRegionItem button = nav.content().item(2, NavRegion.NavRegionItem.class);
        button.shouldHaveLabel("Поиск");
        button.click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/search");
        Selenide.back();
        page.shouldExists();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/");

        // <group label="Пользователь">
        NavRegion.GroupItem groupItem = nav.content().item(3, NavRegion.GroupItem.class);
        groupItem.shouldExists();
        checkGroup(groupItem, page);

        // <dropdown-menu label="Магазин">
        NavRegion.DropdownItem dropdownItem = nav.content().item(4, NavRegion.DropdownItem.class);
        dropdownItem.shouldExists();
        checkDropdown(dropdownItem, page);

        // <group label="Справка">
        groupItem = nav.content().item(5, NavRegion.GroupItem.class);
        groupItem.shouldHaveLabel("Справка");
        groupItem.shouldHaveSize(1);
        groupItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("FAQ");
        groupItem.item(0, NavRegion.NavRegionItem.class).click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/faq");
        Selenide.back();
        page.shouldExists();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/");
    }

    private void checkDropdown(NavRegion.DropdownItem dropdownItem, StandardPage page) {
        dropdownItem.shouldHaveLabel("Магазин");
        dropdownItem.click();
        dropdownItem.shouldBeOpened();
        dropdownItem.shouldHaveSize(5);
        dropdownItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Каталог");
        dropdownItem.item(1, NavRegion.AnchorItem.class).shouldHaveLabel("Акции");
        dropdownItem.item(2, NavRegion.NavRegionItem.class).shouldHaveLabel("Корзина");

        dropdownItem.item(1, NavRegion.NavRegionItem.class).click();
        dropdownItem.shouldBeClosed();
        
        dropdownItem.click();
        dropdownItem.item(2, NavRegion.NavRegionItem.class).click();
        dropdownItem.shouldBeClosed();

        dropdownItem.click();
        NavRegion.GroupItem dropdownGroupItem = dropdownItem.item(3, NavRegion.GroupItem.class);
        dropdownGroupItem.shouldHaveLabel("Категории");
        dropdownGroupItem.shouldHaveSize(5);
        dropdownGroupItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Электроника");
        dropdownGroupItem.item(1, NavRegion.AnchorItem.class).shouldHaveLabel("Одежда");
        dropdownGroupItem.item(1, NavRegion.AnchorItem.class).click();
        Selenide.switchTo().window(0);
        dropdownItem.shouldBeClosed();
        dropdownItem.click();
        dropdownItem.shouldBeOpened();

        dropdownGroupItem.item(2, NavRegion.NavRegionItem.class).shouldHaveLabel("Фильтры");

        NavRegion.DropdownItem dropdownGroupDropdownItem = dropdownGroupItem.item(3, NavRegion.DropdownItem.class);
        dropdownGroupDropdownItem.shouldHaveLabel("Бренды");
        dropdownGroupDropdownItem.click();
        dropdownGroupDropdownItem.shouldBeOpened();
        dropdownGroupDropdownItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Lime");
        dropdownGroupDropdownItem.item(0, NavRegion.NavRegionItem.class).click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/brands/lime");
        Selenide.back();
        page.shouldExists();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/");

        dropdownItem.click();
        dropdownGroupItem.item(4, NavRegion.GroupItem.class).shouldHaveLabel("Ценовые диапазоны");
        dropdownGroupItem.item(4, NavRegion.GroupItem.class).item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("До 1000");

        NavRegion.DropdownItem dropdownDropdownItem = dropdownItem.item(4, NavRegion.DropdownItem.class);
        dropdownDropdownItem.shouldHaveLabel("Администрирование");
        dropdownDropdownItem.click();
        dropdownDropdownItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Статистика");
        dropdownDropdownItem.item(0, NavRegion.NavRegionItem.class).click();
        dropdownItem.shouldBeClosed();
    }

    private void checkGroup(NavRegion.GroupItem groupItem, StandardPage page) {
        groupItem.shouldHaveLabel("Пользователь");
        groupItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Профиль");
        groupItem.item(1, NavRegion.NavRegionItem.class).shouldHaveLabel("Сообщения");
        groupItem.item(2, NavRegion.NavRegionItem.class).shouldHaveLabel("Уведомления");

        NavRegion.DropdownItem groupDropdownItem = groupItem.item(3, NavRegion.DropdownItem.class);
        groupDropdownItem.shouldHaveLabel("Настройки");
        groupDropdownItem.click();
        groupDropdownItem.shouldBeOpened();
        groupDropdownItem.shouldHaveSize(5);
        groupDropdownItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Безопасность");
        groupDropdownItem.item(0, NavRegion.NavRegionItem.class).click();
        groupDropdownItem.shouldBeClosed();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText("security");
        alert.closeButton();

        groupDropdownItem.click();
        groupDropdownItem.item(1, NavRegion.AnchorItem.class).shouldHaveLabel("Конфиденциальность");
        groupDropdownItem.item(2, NavRegion.NavRegionItem.class).shouldHaveLabel("Сброс");

        groupDropdownItem.item(3, NavRegion.GroupItem.class).shouldHaveLabel("Внешний вид");
        groupDropdownItem.item(3, NavRegion.GroupItem.class).shouldHaveSize(1);
        groupDropdownItem.item(3, NavRegion.GroupItem.class).item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Тема");

        NavRegion.DropdownItem groupDropdownDropdownItem = groupDropdownItem.item(4, NavRegion.DropdownItem.class);
        groupDropdownDropdownItem.shouldHaveLabel("Дополнительно");
        groupDropdownDropdownItem.click();
        groupDropdownItem.shouldBeOpened();
        groupDropdownDropdownItem.shouldBeOpened();
        groupDropdownDropdownItem.shouldHaveSize(1);
        groupDropdownDropdownItem.item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Экспорт данных");
        groupDropdownDropdownItem.item(0, NavRegion.NavRegionItem.class).click();
        groupDropdownDropdownItem.shouldBeClosed();
        groupDropdownItem.shouldBeClosed();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/\\?test=export");

        groupItem.item(4, NavRegion.GroupItem.class).shouldHaveLabel("Действия");
        groupItem.item(4, NavRegion.GroupItem.class).item(0, NavRegion.NavRegionItem.class).shouldHaveLabel("Выйти");
    }
}