package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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
 * Автотест для действия открытия страницы
 */
public class OpenPageAT extends AutoTestBase {

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
    public void testFilterState() {
        setJsonPath("net/n2oapp/framework/autotest/action/open_page/simple");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/open_page/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/simple/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/simple/open.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.breadcrumb().crumb(0).shouldHaveLabel("Сохранение фильтров при возврате назад");
        TableWidget table = page.widget(TableWidget.class);

        TableWidget.Rows rows = table.columns().rows();
        InputText nameFilter = table.filters().fields().field("name").control(InputText.class);

        nameFilter.shouldBeEmpty();
        rows.shouldHaveSize(4);
        nameFilter.click();
        nameFilter.setValue("test3");
        table.filters().toolbar().button("Найти").click();
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).shouldHaveText("test3");

        rows.row(0).click();
        StandardPage open = N2oSelenide.page(StandardPage.class);
        open.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        TableWidget openPageTable = open.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        TableWidget.Rows openPageTableRows = openPageTable.columns().rows();
        InputText openPageTypeFilter = openPageTable.filters().fields().field("type").control(InputText.class);

        openPageTypeFilter.shouldBeEmpty();
        openPageTableRows.shouldHaveSize(4);
        openPageTypeFilter.click();
        openPageTypeFilter.setValue("2");
        openPageTable.filters().toolbar().button("Найти").click();
        openPageTableRows.shouldHaveSize(2);

        open.breadcrumb().crumb(0).click();
        page.breadcrumb().shouldHaveSize(1);
        page.breadcrumb().crumb(0).shouldHaveLabel("Сохранение фильтров при возврате назад");

        // preserving filters when going back
        nameFilter.shouldHaveValue("test3");
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).shouldHaveText("test3");
        rows.row(0).click();

        // reset filters on return forward
        open.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        openPageTypeFilter.shouldBeEmpty();
        openPageTableRows.shouldHaveSize(4);
    }

    @Test
    public void testResolveBreadcrumb() {
        setJsonPath("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/simple_page/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/widget.widget.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/page.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");
        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().row(0).click();
        table.toolbar().topLeft().button("Открыть").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(1).shouldHaveLabel("test1");
    }

    @Test
    public void testResolveBreadcrumbOnStandardPage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/standard_page/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/widget.widget.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/resolve_breadcrumb/page.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().row(0).click();
        table.toolbar().topLeft().button("Открыть").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.widget(FormWidget.class).shouldExists();
        page.breadcrumb().crumb(1).shouldHaveLabel("test1");
    }

    @Test
    public void testTargetNewWindow() {
        setJsonPath("net/n2oapp/framework/autotest/action/open_page/target/new_window");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/target/new_window/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/target/new_window/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/target/new_window/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();

        table.columns().rows().row(0).click();
        table.toolbar().topLeft().button("Открыть").click();

        Selenide.switchTo().window(1);
        page.shouldExists();
        page.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/1/open");

        page.widget(FormWidget.class).fields().field("id").control(InputText.class).shouldHaveValue("1");
        page.widget(FormWidget.class).fields().field("name").control(InputText.class).shouldHaveValue("test1");
        Selenide.closeWindow();
    }

    @Test
    public void testNestedRoutesWithPathParameters() {
        setJsonPath("net/n2oapp/framework/autotest/action/open_page/nested_routes");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/open_page/nested_routes/book.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/nested_routes/books.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/nested_routes/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/nested_routes/reader.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/nested_routes/readers.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование вложенных роутов с path-параметрами");
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().row(0).click();
        table.toolbar().topLeft().button("Open").click();

        page.breadcrumb().crumb(1).shouldHaveLabel("Первая вложенная страница");
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/1/reader");

        table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().row(1).click();
        table.toolbar().topLeft().button("Open").click();

        page.breadcrumb().crumb(2).shouldHaveLabel("Вторая вложенная страница");
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/1/reader/2/book");
    }
}
