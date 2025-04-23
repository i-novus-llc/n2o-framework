package net.n2oapp.framework.autotest.access.schema;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class HeaderAccessAT extends AutoTestBase {

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
        setResourcePath("net/n2oapp/framework/autotest/access/schema/header");
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack(), new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/tutorial.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/menu1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/menu2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/menu3.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/menu4.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/header/schema.access.xml"));
    }

    @Test
    void testAdminAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singleton("admin"));
        user.put("permissions", Collections.singleton("edit"));
        setUserInfo(user);
        builder.getEnvironment().getContextProcessor().set("username", user.get("username"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Шапка");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        page.header().nav().shouldHaveSize(4);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.shouldHaveLabel("Доступно всем");
        menuItem0.shouldHaveUrl(getBaseUrl() + "/#/menu1");
        menuItem0.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная всем");

        AnchorMenuItem menuItem1 = page.header().nav().anchor(1);
        menuItem1.shouldHaveLabel("Требуется роль admin");
        menuItem1.shouldHaveUrl(getBaseUrl() + "/#/menu2");
        menuItem1.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная с ролью admin");

        AnchorMenuItem menuItem2 = page.header().nav().anchor(2);
        menuItem2.shouldHaveLabel("Требуется право edit");
        menuItem2.shouldHaveUrl(getBaseUrl() + "/#/menu3");
        menuItem2.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная с правом edit");

        page.header().extra().shouldHaveSize(1);
        AnchorMenuItem extraLink = page.header().extra().item(0, AnchorMenuItem.class);
        extraLink.shouldHaveLabel("Admin");
        extraLink.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        page.header().nav().dropdown(3).shouldExists();
        page.header().nav().dropdown(3).click();
        page.header().nav().dropdown(3).shouldHaveSize(1);
        page.header().nav().dropdown(3).item(0).shouldHaveLabel("Требуется роль admin");
    }

    @Test
    void testAnonymousAccess() {
        setUserInfo(null);
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Шапка");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        page.header().nav().shouldHaveSize(2);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.shouldHaveLabel("Доступно всем");
        menuItem0.shouldHaveUrl(getBaseUrl() + "/#/menu1");
        menuItem0.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная всем");

        AnchorMenuItem menuItem3 = page.header().nav().anchor(1);
        menuItem3.shouldHaveLabel("Только анонимам");
        menuItem3.shouldHaveUrl(getBaseUrl() + "/#/menu4");
        menuItem3.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная только анонимам");
        page.header().nav().shouldHaveSize(2);
    }
}
