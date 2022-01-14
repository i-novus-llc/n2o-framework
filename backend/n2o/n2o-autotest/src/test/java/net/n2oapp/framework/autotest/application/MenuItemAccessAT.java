package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.access.metadata.compile.SecurityExtensionAttributeMapper;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MenuItemAccessAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack());
        builder.extensions(new SecurityExtensionAttributeMapper());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/testMenu.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/access/app.application.xml"));
    }

    @Test
    public void testAdminAccess() {
        setUserInfo(loadAdminInfo());

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");

        page.header().nav().shouldHaveSize(3);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldExists();
        menuItem.labelShouldHave("Доступно всем пользователям");

        menuItem = page.header().nav().anchor(1);
        menuItem.shouldExists();
        menuItem.labelShouldHave("Доступно с ролью admin");

        DropdownMenuItem dropdown = page.header().nav().dropdown(2);
        dropdown.shouldExists();
        dropdown.labelShouldHave("Доступно с правом edit");
    }

    @Test
    public void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Главная страница");

        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldExists();
        menuItem.labelShouldHave("Доступно всем пользователям");
    }

    private Map<String, Object> loadAdminInfo() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singletonList("admin"));
        user.put("permissions", Collections.singletonList("edit"));
        return user;
    }
}
