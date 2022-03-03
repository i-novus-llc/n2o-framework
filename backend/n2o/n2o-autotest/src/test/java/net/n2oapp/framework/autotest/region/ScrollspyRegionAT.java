package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.ScrollspyRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Configuration.headless;

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
        headless = false;//fixme
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/scrollspy/index.page.xml"));
    }

    @Test
    public void testScrollspyRegion() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Регион с автоматически прокручиваемым меню");

        ScrollspyRegion region = page.regions().region(0, ScrollspyRegion.class);
        region.shouldExists();
        ScrollspyRegion.Menu menu = region.menu();
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

        menu.itemShouldBeActive("Личные данные");
        region.contentItem("Личные данные").shouldBeVisible();
        region.contentItem("Дополнительная информация").shouldBeVisible();

//        menu.menuItem("Дополнительная информация").click();
//        region.contentItem("Личные данные").shouldBeHidden();
//        region.contentItem("Дополнительная информация").shouldBeVisible();
    }
}
