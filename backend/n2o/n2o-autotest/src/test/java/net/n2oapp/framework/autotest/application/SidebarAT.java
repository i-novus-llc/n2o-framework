package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.api.metadata.application.NavigationLayoutEnum;
import net.n2oapp.framework.api.metadata.application.SidebarStateEnum;
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
class SidebarAT extends AutoTestBase {

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
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oApplicationPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oActionsPack()
        );
    }

    @Test
    void testSidebar() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/simple/test.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/simple/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.shouldHaveSidebarSwitcher();
        header.switchSidebar();
        page.sidebar().shouldExists();
        page.sidebar().shouldHaveTitle("Лого");
        page.sidebar().shouldHaveBrandLogo("images/logoWhite.png");
        header.switchSidebar();
        page.sidebar().shouldHaveState(SidebarStateEnum.NONE);
    }

    @Test
    void testFullsizeSidebar() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fullsize/fullsize.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fullsize/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.shouldHaveSidebarSwitcher();
        header.switchSidebar();
        page.shouldHaveLayout(NavigationLayoutEnum.fullSizeSidebar);
    }

    @Test
    void testFixedSidebar() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fixed/fixed.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fixed/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.shouldHaveSidebarSwitcher();
        header.switchSidebar();
        page.sidebar().shouldBeFixed();
    }

    @Test
    void testRightSidebar() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/right/right.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/right/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.shouldHaveSidebarSwitcher();
        header.switchSidebar();
        page.sidebar().shouldBeRight();
    }

    @Test
    void testSidebarOverlay() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/overlay/overlay.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/overlay/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.shouldHaveSidebarSwitcher();
        header.switchSidebar();
        page.sidebar().shouldBeOverlay();
    }

    @Test
    void testSidebarDefaultMicro() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_micro/default_micro.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_micro/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.sidebar().shouldExists();
        page.sidebar().shouldHaveState(SidebarStateEnum.MICRO);
    }

    @Test
    void testSidebarDefaultMini() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_mini/default_mini.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_mini/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.sidebar().shouldExists();
        page.sidebar().shouldHaveState(SidebarStateEnum.MINI);
    }
}
