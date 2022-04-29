package net.n2oapp.framework.sandbox.autotest.schema;

import lombok.SneakyThrows;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(properties = {
        "n2o.engine.test.classpath=/access/schema/header/",
        "n2o.sandbox.project-id=access_schema_header"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeaderAT extends SandboxAutotestBase {

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
        builder.packs(new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        addRuntimeProperty("n2o.access.schema.id", "schema");
        super.configure(builder);
    }

    @Test
    public void testAdminAccess() {
        Map<String, Object> testUser = loadUser();
        setUserInfo(testUser);
        builder.getEnvironment().getContextProcessor().set("username", testUser.get("username"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("Хедер");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");

        page.header().nav().shouldHaveSize(3);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.labelShouldHave("Доступно всем");
        menuItem0.urlShouldHave(getBaseUrl() + "/#/mi1");
        menuItem0.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная всем");

        AnchorMenuItem menuItem1 = page.header().nav().anchor(1);
        menuItem1.labelShouldHave("Требуется роль admin");
        menuItem1.urlShouldHave(getBaseUrl() + "/#/mi2");
        menuItem1.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная с ролью admin");

        AnchorMenuItem menuItem2 = page.header().nav().anchor(2);
        menuItem2.labelShouldHave("Требуется право edit");
        menuItem2.urlShouldHave(getBaseUrl() + "/#/mi3");
        menuItem2.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная с правом edit");

        page.header().extra().shouldHaveSize(1);
        AnchorMenuItem extraLink = page.header().extra().item(0, AnchorMenuItem.class);
        extraLink.labelShouldHave((String) testUser.get("username"));
    }

    @Test
    public void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("Хедер");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");

        page.header().nav().shouldHaveSize(2);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.labelShouldHave("Доступно всем");
        menuItem0.urlShouldHave(getBaseUrl() + "/#/mi1");
        menuItem0.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная всем");

        AnchorMenuItem menuItem3 = page.header().nav().anchor(1);
        menuItem3.labelShouldHave("Только анонимам");
        menuItem3.urlShouldHave(getBaseUrl() + "/#/mi4");
        menuItem3.click();
        page.breadcrumb().titleShouldHaveText("Страница доступная только анонимам");
    }

    @SneakyThrows
    private Map<String, Object> loadUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singletonList("admin"));
        user.put("permissions", Collections.singletonList("edit"));
        return user;
    }

}
