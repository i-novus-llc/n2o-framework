package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "server.servlet.context-path=/", classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TabsAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"),
                new CompileInfo("/examples/tabs/index.page.xml"));
    }

    @Test
    public void testTabs() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Вкладки");

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        tabs.tab(0).shouldHaveName("Один");
        tabs.tab(0).shouldBeActive();
        tabs.tab(1).click();
        tabs.tab(1).shouldHaveName("Два");
        tabs.tab(1).shouldBeActive();
        tabs.tab(2).click();
        tabs.tab(2).shouldHaveName("Три");
        tabs.tab(2).shouldBeActive();
    }

}

