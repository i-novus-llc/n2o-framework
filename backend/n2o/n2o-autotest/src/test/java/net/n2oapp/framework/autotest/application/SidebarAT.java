package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест Боковой панели
 */
public class SidebarAT extends AutoTestBase {

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
                new N2oControlsPack());
    }

    @Test
    public void testSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/test.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExist();
        header.switchSidebar();
        page.sidebar().shouldExists();
    }
}
