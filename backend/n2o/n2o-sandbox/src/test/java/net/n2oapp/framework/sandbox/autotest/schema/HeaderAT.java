package net.n2oapp.framework.sandbox.autotest.schema;

import lombok.SneakyThrows;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(properties = {"n2o.engine.test.classpath=/access/schema/header/"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack(), new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        builder.sources(new CompileInfo("access/schema/header/tutorial.application.xml"),
                new CompileInfo("access/schema/header/index.page.xml"),
                new CompileInfo("access/schema/header/menu1.page.xml"),
                new CompileInfo("access/schema/header/menu2.page.xml"),
                new CompileInfo("access/schema/header/menu3.page.xml"),
                new CompileInfo("access/schema/header/menu4.page.xml"),
                new CompileInfo("access/schema/header/schema.access.xml"));
    }

    @Test
    public void testAdminAccess() {
        Map<String, Object> testUser = loadUser();
        setUserInfo(testUser);
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "schema");
        builder.getEnvironment().getContextProcessor().set("username", testUser.get("username"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Хедер");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        page.header().nav().shouldHaveSize(3);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.shouldHaveLabel("Доступно всем");
        menuItem0.shouldHaveUrl(getBaseUrl() + "/#/mi1");
        menuItem0.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная всем");

        AnchorMenuItem menuItem1 = page.header().nav().anchor(1);
        menuItem1.shouldHaveLabel("Требуется роль admin");
        menuItem1.shouldHaveUrl(getBaseUrl() + "/#/mi2");
        menuItem1.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная с ролью admin");

        AnchorMenuItem menuItem2 = page.header().nav().anchor(2);
        menuItem2.shouldHaveLabel("Требуется право edit");
        menuItem2.shouldHaveUrl(getBaseUrl() + "/#/mi3");
        menuItem2.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная с правом edit");

        page.header().extra().shouldHaveSize(1);
        AnchorMenuItem extraLink = page.header().extra().item(0, AnchorMenuItem.class);
        extraLink.shouldHaveLabel((String) testUser.get("username"));
    }

    @Test
    public void testAnonymousAccess() {
        setUserInfo(null);
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "schema");

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("Хедер");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Главная страница");

        page.header().nav().shouldHaveSize(2);

        AnchorMenuItem menuItem0 = page.header().nav().anchor(0);
        menuItem0.shouldHaveLabel("Доступно всем");
        menuItem0.shouldHaveUrl(getBaseUrl() + "/#/mi1");
        menuItem0.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная всем");

        AnchorMenuItem menuItem3 = page.header().nav().anchor(1);
        menuItem3.shouldHaveLabel("Только анонимам");
        menuItem3.shouldHaveUrl(getBaseUrl() + "/#/mi4");
        menuItem3.click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница доступная только анонимам");
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
