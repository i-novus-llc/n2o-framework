package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест шапки(header)
 */
class  SimpleHeaderAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oActionsPack());

        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/header/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/header/testPage1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/header/test.application.xml"));
    }

    @Test
    void simpleHeader() {
                String rootUrl = getBaseUrl();
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Лого");
        page.header().nav().shouldHaveSize(2);
        AnchorMenuItem link = page.header().nav().anchor(0);
        link.shouldHaveLabel("ссылка");
        link.shouldHaveUrl(rootUrl + "/");

        DropdownMenuItem dropdown = page.header().nav().dropdown(1);
        dropdown.shouldHaveLabel("список");
        dropdown.click();
        dropdown.item(0).shouldHaveLabel("Название страницы");
        dropdown.item(0).shouldHaveUrl(rootUrl + "/#/pageRoute");
        dropdown.item(0).click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Название страницы");

        dropdown.click();
        dropdown.item(1).shouldHaveLabel("элемент списка №2");
        dropdown.item(1).shouldHaveUrl(rootUrl + "/#/pageRoute1");
        dropdown.item(1).click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Вторая страница");

        page.header().extra().shouldHaveSize(1);
        link = page.header().extra().item(0, AnchorMenuItem.class);
        link.shouldHaveLabel("ссылка из extra-menu");
        link.shouldHaveUrl(getBaseUrl() + "/");
    }

    @Test
    void multiLevelDropdown() {
        String rootUrl = getBaseUrl();
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Лого");
        page.header().nav().shouldHaveSize(2);

        DropdownMenuItem dropdown = page.header().nav().dropdown(1);
        dropdown.shouldHaveLabel("список");
        dropdown.click();
        DropdownMenuItem dropdownLevelTwo = dropdown.item(2, DropdownMenuItem.class);
        dropdownLevelTwo.shouldHaveLabel("Многоуровневый список");
        dropdownLevelTwo.click();
        dropdown.item(2, DropdownMenuItem.class).shouldHaveSize(2);
        page.breadcrumb().crumb(0).shouldHaveLabel("Название страницы");

        dropdownLevelTwo.item(0).shouldHaveLabel("Название страницы");
        dropdownLevelTwo.item(0).shouldHaveUrl(rootUrl + "/#/pageRoute");
        dropdownLevelTwo.item(1).shouldHaveLabel("элемент многоуровнегосписка №2");
        dropdownLevelTwo.item(1).shouldHaveUrl(rootUrl + "/#/pageRoute1");
        dropdownLevelTwo.item(1).click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Вторая страница");
    }
}
