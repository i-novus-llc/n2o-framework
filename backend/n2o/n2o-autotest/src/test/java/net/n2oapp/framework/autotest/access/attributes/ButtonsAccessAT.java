package net.n2oapp.framework.autotest.access.attributes;

import lombok.SneakyThrows;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ButtonsAccessAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack(), new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/access/attributes/buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/buttons/default.access.xml"));
    }

    @Test
    void testAdminAccess() {
        setUserInfo(loadUser());

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к кнопкам по sec атрибутам");

        Toolbar topLeft = page.widget(FormWidget.class).toolbar().topLeft();
        topLeft.shouldHaveSize(3);

        StandardButton button = topLeft.button(0, StandardButton.class);
        button.shouldExists();
        button.shouldHaveLabel("Доступно всем");
        button.shouldBeEnabled();

        button = topLeft.button(1, StandardButton.class);
        button.shouldExists();
        button.shouldHaveLabel("Только с ролью admin");
        button.shouldBeEnabled();

        button = topLeft.button(2, StandardButton.class);
        button.shouldExists();
        button.shouldHaveLabel("Только с правом edit");
        button.shouldBeEnabled();
    }

    @Test
    void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к кнопкам по sec атрибутам");

        Toolbar topLeft = page.widget(FormWidget.class).toolbar().topLeft();
        topLeft.shouldHaveSize(2);

        StandardButton button = topLeft.button(0, StandardButton.class);
        button.shouldExists();
        button.shouldHaveLabel("Доступно всем");
        button.shouldBeEnabled();

        button = topLeft.button(1, StandardButton.class);
        button.shouldExists();
        button.shouldHaveLabel("Только анонимам");
        button.shouldBeEnabled();
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
