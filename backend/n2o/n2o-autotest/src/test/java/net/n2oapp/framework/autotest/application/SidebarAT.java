package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.api.metadata.application.SidebarState;
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/simple/test.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldExists();
        page.sidebar().brandNameShouldBe("Лого");
        page.sidebar().brandLogoShouldBe("images/logoWhite.png");
        header.switchSidebar();
        page.sidebar().shouldHaveState(SidebarState.none);
    }

    @Test
    public void testFullsizeSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fullsize/fullsize.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fullsize/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.shouldHaveLayout(NavigationLayout.fullSizeSidebar);
    }

    @Test
    public void testFixedSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fixed/fixed.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/fixed/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldBeFixed();
    }

    @Test
    public void testRightSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/right/right.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/right/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldBeRight();
    }

    @Test
    public void testSidebarOverlay() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/overlay/overlay.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/overlay/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldBeOverlay();
    }

    @Test
    public void testSidebarDefaultMicro() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_micro/default_micro.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_micro/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.sidebar().shouldExists();
        page.sidebar().shouldHaveState(SidebarState.micro);
    }

    @Test
    public void testSidebarDefaultMini() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_mini/default_mini.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebar/default_mini/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.sidebar().shouldExists();
        page.sidebar().shouldHaveState(SidebarState.mini);
    }
}
