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

public class AccessHeaderAT extends AutoTestBase {

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
    public void testAdminAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singleton("admin"));
        user.put("permissions", Collections.singleton("edit"));
        setUserInfo(user);
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "schema");
        builder.getEnvironment().getContextProcessor().set("username", user.get("username"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("Шапка");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");

        page.header().nav().shouldHaveSize(3);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.labelShouldHave("Доступно всем");
        menuItem0.urlShouldHave(getBaseUrl() + "/#/menu1");
        menuItem0.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная всем");

        AnchorMenuItem menuItem1 = page.header().nav().anchor(1);
        menuItem1.labelShouldHave("Требуется роль admin");
        menuItem1.urlShouldHave(getBaseUrl() + "/#/menu2");
        menuItem1.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная с ролью admin");

        AnchorMenuItem menuItem2 = page.header().nav().anchor(2);
        menuItem2.labelShouldHave("Требуется право edit");
        menuItem2.urlShouldHave(getBaseUrl() + "/#/menu3");
        menuItem2.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная с правом edit");

        page.header().extra().shouldHaveSize(1);
        AnchorMenuItem extraLink = page.header().extra().item(0, AnchorMenuItem.class);
        extraLink.labelShouldHave("Admin");
        extraLink.click();
        page.breadcrumb().titleShouldHaveText("Главная страница");
    }

    @Test
    public void testAnonymousAccess() {
        setUserInfo(null);

        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "schema");

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("Шапка");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");

        page.header().nav().shouldHaveSize(2);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.labelShouldHave("Доступно всем");
        menuItem0.urlShouldHave(getBaseUrl() + "/#/menu1");
        menuItem0.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная всем");

        AnchorMenuItem menuItem3 = page.header().nav().anchor(1);
        menuItem3.labelShouldHave("Только анонимам");
        menuItem3.urlShouldHave(getBaseUrl() + "/#/menu4");
        menuItem3.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная только анонимам");
    }

}
