package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
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
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
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
        page.sidebar().titleShouldBe("Лого");
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
        page.sidebar().titleShouldBe("Лого");
        page.sidebar().brandLogoShouldBe("images/logoWhite.png");
        header.switchSidebar();
        page.sidebar().shouldHaveState(SidebarState.none);
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        form.toolbar().bottomLeft().button("Open").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        SimpleHeader openHeader = page.header();
        openHeader.shouldExists();
        openHeader.sidebarSwitcherShouldExists();
        openHeader.switchSidebar();
        open.urlShouldMatches(getBaseUrl() + "/#/persons");
        open.sidebar().shouldExists();
        open.sidebar().titleShouldBe("Боковая панель для страницы Persons");
        open.sidebar().brandLogoShouldBe("images/logoPersons.png");
    }

    @Test
    public void testDynamicPathsSidebar(){
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/multi_sidebar/multi_sidebar.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/multi_sidebar/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/multi_sidebar/page.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldExists();
        page.sidebar().titleShouldBe("Лого");
        page.sidebar().brandLogoShouldBe("images/logoWhite.png");
        header.switchSidebar();
        page.sidebar().shouldHaveState(SidebarState.none);
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        form.toolbar().bottomLeft().button("Open").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        SimpleHeader openHeader = page.header();
        openHeader.shouldExists();
        openHeader.sidebarSwitcherShouldExists();
        openHeader.switchSidebar();
        open.urlShouldMatches(getBaseUrl() + "/#/persons/1/list");
        open.sidebar().shouldExists();
        open.sidebar().titleShouldBe("Лист");
        open.sidebar().brandLogoShouldBe("images/logoList.png");
    }

    @Test
    public void testAllRegxPathSidebar(){
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/all_regx_path/all_regx_path.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/all_regx_path/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/all_regx_path/page.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldExists();
        page.sidebar().titleShouldBe("Лого");
        page.sidebar().brandLogoShouldBe("images/logoWhite.png");
        header.switchSidebar();
        page.sidebar().shouldHaveState(SidebarState.none);
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        form.toolbar().bottomLeft().button("Open").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        SimpleHeader openHeader = page.header();
        openHeader.shouldExists();
        openHeader.sidebarSwitcherShouldExists();
        openHeader.switchSidebar();
        open.urlShouldMatches(getBaseUrl() + "/#/persons/user");
        open.sidebar().shouldExists();
        open.sidebar().titleShouldBe("Пользователь");
        open.sidebar().brandLogoShouldBe("images/logoUser.png");
    }

    @Test
    public void testOverlappingPathsSidebar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/overlapping_case/overlapping_case.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/overlapping_case/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/overlapping_case/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/overlapping_case/list.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        SimpleHeader header = page.header();
        header.shouldExists();
        header.sidebarSwitcherShouldExists();
        header.switchSidebar();
        page.sidebar().shouldExists();
        page.sidebar().titleShouldBe("Лого");
        page.sidebar().brandLogoShouldBe("images/logoWhite.png");
        header.switchSidebar();
        page.sidebar().shouldHaveState(SidebarState.none);
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        form.toolbar().bottomLeft().button("Open").click();

        SimplePage list = N2oSelenide.page(SimplePage.class);
        list.shouldExists();
        list.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        SimpleHeader openHeader = page.header();
        openHeader.shouldExists();
        openHeader.sidebarSwitcherShouldExists();
        openHeader.switchSidebar();
        list.urlShouldMatches(getBaseUrl() + "/#/persons/1/list");
        list.sidebar().shouldExists();
        list.sidebar().titleShouldBe("Лист");
        list.sidebar().brandLogoShouldBe("images/logoList.png");
        openHeader.switchSidebar();
        list.sidebar().shouldHaveState(SidebarState.none);

        form = list.widget(FormWidget.class);
        form.shouldExists();
        form.toolbar().bottomLeft().button("Open").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().crumb(2).shouldHaveLabel("Третья страница");
        openHeader = page.header();
        openHeader.shouldExists();
        openHeader.sidebarSwitcherShouldExists();
        openHeader.switchSidebar();
        open.sidebar().shouldExists();
        open.sidebar().titleShouldBe("Профиль");
        open.sidebar().brandLogoShouldBe("images/logoPerson.png");
    }

    @Test
    public void testSidebarWithDatasource() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/datasource/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/datasource/docs.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/datasource/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/datasource/profile.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/sidebars/datasource/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование источника данных в сайдбаре");
        Sidebar sidebar = page.sidebar();
        sidebar.shouldBeHidden();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(0).click();
        table.toolbar().topLeft().button("Открыть").click();

        page.shouldExists();
        page.urlShouldMatches(getBaseUrl() + "/#/person/1/profile");
        page.breadcrumb().crumb(1).shouldHaveLabel("Страница профиля");
        sidebar.shouldExists();
        sidebar.shouldHaveState(SidebarState.mini);
        sidebar.titleShouldBe("test1");
        sidebar.subtitleShouldBe("type11");
        sidebar.clickToggleBtn();
        AnchorMenuItem menuItem = sidebar.nav().anchor(0);
        menuItem.shouldExists();
        menuItem.labelShouldHave("Документы");
        menuItem.click();

        page.shouldExists();
        page.urlShouldMatches(getBaseUrl() + "/#/person/1/docs\\?name=test1");
        page.breadcrumb().crumb(0).shouldHaveLabel("Документы");
        sidebar.titleShouldBe("test1");
        sidebar.subtitleShouldBe("type11");
    }
}
