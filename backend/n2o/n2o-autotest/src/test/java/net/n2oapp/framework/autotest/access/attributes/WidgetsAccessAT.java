package net.n2oapp.framework.autotest.access.attributes;

import lombok.SneakyThrows;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
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

class WidgetsAccessAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/access/attributes/widgets/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/widgets/default.access.xml"));
    }

    @Test
    void testAdminAccess() {
        setUserInfo(loadUser());

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к виджетам по sec атрибутам");

        Regions regions = page.regions();
        regions.shouldHaveSize(2);
        PanelRegion panel = regions.region(0, PanelRegion.class);
        panel.shouldExists();
        panel.shouldHaveTitle("Доступно всем");

        TabsRegion tabs = regions.region(1, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("Доступно с ролью admin");
        tabs.tab(0).shouldBeActive();
        tabs.tab(1).click();
        tabs.tab(1).shouldHaveName("Доступно с правом edit");
        tabs.tab(1).shouldBeActive();
    }

    @Test
    void testAnonymousAccess() {
        setUserInfo(null);

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к виджетам по sec атрибутам");

        Regions regions = page.regions();
        //todo региона должно быть 2 https://jira.i-novus.ru/browse/NNO-10835
        regions.shouldHaveSize(3);
        PanelRegion panel = regions.region(0, PanelRegion.class);
        panel.shouldExists();
        panel.shouldHaveTitle("Доступно всем");

        LineRegion line = regions.region(2, LineRegion.class);
        line.shouldExists();
        line.shouldHaveLabel("Доступно анонимам");
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
