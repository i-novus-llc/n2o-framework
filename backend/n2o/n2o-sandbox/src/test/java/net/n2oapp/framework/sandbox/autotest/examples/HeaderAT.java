package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HeaderAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("/examples/header/tutorial.application.xml"),
                new CompileInfo("/examples/header/index.page.xml"),
                new CompileInfo("/examples/header/menu1.page.xml"),
                new CompileInfo("/examples/header/menu2.page.xml"),
                new CompileInfo("/examples/header/menu3.page.xml"),
                new CompileInfo("/examples/header/menu4.page.xml"));
    }

    @Test
    public void testHeaderMenu() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Хедер");
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        page.header().nav().shouldHaveSize(3);

        AnchorMenuItem menu1 = page.header().nav().anchor(0);
        menu1.shouldHaveLabel("Первая страница");
        menu1.shouldHaveUrl(getBaseUrl() + "/#/mi1");
        menu1.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");

        AnchorMenuItem menu2 = page.header().nav().anchor(1);
        menu2.shouldHaveLabel("Вторая страница");
        menu2.shouldHaveUrl(getBaseUrl() + "/#/mi2");
        menu2.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Вторая страница");

        DropdownMenuItem dropdown = page.header().nav().dropdown(2);
        dropdown.click();
        dropdown.shouldHaveLabel("Вложенное меню");
        dropdown.item(0).shouldHaveLabel("Третья страница");
        dropdown.item(0).shouldHaveUrl(getBaseUrl() + "/#/mi4");
        dropdown.item(0).click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Третья страница");

        dropdown.click();
        dropdown.shouldHaveLabel("Вложенное меню");
        dropdown.item(1).shouldHaveLabel("Четвертая страница");
        dropdown.item(1).shouldHaveUrl(getBaseUrl() + "/#/mi5");
        dropdown.item(1).click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Четвертая страница");

        page.header().extra().shouldHaveSize(1);
        AnchorMenuItem extraLink = page.header().extra().item(0, AnchorMenuItem.class);
        extraLink.shouldHaveLabel("Внешняя ссылка");
        extraLink.shouldHaveUrl("http://example.com/");
        extraLink.click();
        page.shouldHaveUrlMatches("http://example.com/");
    }
}

