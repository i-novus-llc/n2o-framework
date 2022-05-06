package net.n2oapp.framework.autotest.application;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест множественных боковых панелей
 */
public class SidebarsAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Configuration.headless = false;
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack());
    }

    @Test
    public void testSingleSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/single_sidebar/single_sidebar.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/single_sidebar/index.page.xml"));
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
    public void testNoPathSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/no_path_sidebars/no_path_sidebars.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/no_path_sidebars/index.page.xml"));
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
    public void testDefaultAndPathSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/default_and_path/default_and_path.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/default_and_path/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/default_and_path/persons.page.xml"));
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
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        form.toolbar().bottomLeft().button("Open").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().titleShouldHaveText("Вторая страница");
        SimpleHeader openHeader = page.header();
        openHeader.shouldExists();
        openHeader.sidebarSwitcherShouldExists();
        openHeader.switchSidebar();
        open.urlShouldMatches(getBaseUrl() + "/#/persons");
        open.sidebar().shouldExists();
        open.sidebar().brandNameShouldBe("Persons Sidebar");
    }




}
