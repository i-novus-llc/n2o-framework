package net.n2oapp.framework.autotest.breadcrumbs;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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

public class BreadcrumbsAT extends AutoTestBase {

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
    public void breadcrumbsTest() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/reader.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/book.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/readers.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/books.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Table");
        checkPageAndClickRow(page, "reader1", "reader2", 0);

        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Table");
        page.breadcrumb().crumb(1).shouldHaveLabel("reader1");
        checkPageAndClickRow(page, "book1", "book2", 1);

        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Table");
        page.breadcrumb().crumb(1).shouldHaveLabel("reader1");
        page.breadcrumb().crumb(2).shouldHaveLabel("book2");
        checkPageAndClickRow(page, "reader1", "reader2", 1);

        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Table");
        page.breadcrumb().crumb(1).shouldHaveLabel("reader2");
        checkPageAndClickRow(page, "book1", "book2", 0);

        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Table");
        page.breadcrumb().crumb(1).shouldHaveLabel("reader2");
        page.breadcrumb().crumb(2).shouldHaveLabel("book1");
    }

    @Test
    public void testPage() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/page/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/page/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/page/page3.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.breadcrumb().shouldHaveSize(2);
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест настройки бредкрамба на странице");
        page.toolbar().bottomLeft().button("Вторая страница").click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");
        page.breadcrumb().crumb(0).shouldHaveLink(getBaseUrl() + "/#");
        page.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        page.breadcrumb().crumb(1).shouldHaveLink(getBaseUrl() + "/#/page2");
        page.breadcrumb().crumb(1).click();
        page.shouldExists();

        page.breadcrumb().crumb(0).click();
        page.shouldExists();
        page.breadcrumb().shouldHaveSize(1);
        page.breadcrumb().crumb(0).shouldHaveLabel("Тест настройки бредкрамба на странице");
        page.toolbar().bottomLeft().button("Вторая страница").click();
        page.toolbar().bottomLeft().button("Третья страница").click();
        page.breadcrumb().shouldHaveSize(3);
        page.breadcrumb().crumb(0).shouldHaveLabel("Вторая страница");
        page.breadcrumb().crumb(0).shouldHaveLink(getBaseUrl() + "/#/page2");
        page.breadcrumb().crumb(1).shouldHaveLabel("Третья страница");
        page.breadcrumb().crumb(1).shouldHaveLink(getBaseUrl() + "/#/page2/page3");
        page.breadcrumb().crumb(2).shouldHaveLabel("Нет ссылки");
        page.breadcrumb().crumb(2).shouldNotHaveLink();

    }

    @Test
    public void testOpenPage() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/open_page/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/open_page/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/open_page/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.widget(TableWidget.class).columns().rows().row(2).click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");
        page.breadcrumb().crumb(0).shouldHaveLink(getBaseUrl() + "/#/");
        page.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница test3 open-page");
        page.breadcrumb().crumb(1).shouldHaveLink(getBaseUrl() + "/#/3/page2");
        page.breadcrumb().crumb(0).click();
        page.shouldExists();
        page.widget(TableWidget.class).shouldExists();
    }

    @Test
    public void testRelativeLinks() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/relative_links/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/relative_links/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/relative_links/page3.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        String url = getBaseUrl();
        page.toolbar().bottomLeft().button("Вторая страница").click();
        page.breadcrumb().crumb(0).shouldHaveLabel("Первая страница");
        page.breadcrumb().crumb(0).shouldHaveLink(getBaseUrl() + "/#");
        page.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        page.breadcrumb().crumb(1).shouldHaveLink(getBaseUrl() + "/#/page2");

        page.breadcrumb().crumb(0).click();
        page.shouldExists();
        page.urlShouldMatches(url + "/#/");
        page.toolbar().bottomLeft().button("Вторая страница").click();
        page.shouldExists();
        page.toolbar().bottomLeft().button("Третья страница").click();
        page.breadcrumb().crumb(0).shouldHaveLink(getBaseUrl() + "/#");
        page.breadcrumb().crumb(1).shouldHaveLink(getBaseUrl() + "/#/page2");
    }

    @Test
    public void testResolve() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/resolve/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/resolve/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/breadcrumbs/resolve/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class).columns().rows().row(2).click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Таблица");
        page.breadcrumb().crumb(0).shouldHaveLink(getBaseUrl() + "/#/");
        page.breadcrumb().crumb(1).shouldHaveLabel("test3");
        page.breadcrumb().crumb(1).shouldHaveLink(getBaseUrl() + "/#/3/page2");
        page.titleShouldHaveText("2");
    }

    private void checkPageAndClickRow(SimplePage page, String firstTableRow, String secondTableRow, Integer clickRow) {
        StandardButton openButton = page.widget(FormWidget.class)
                .toolbar()
                .topLeft()
                .button(0, StandardButton.class);
        openButton.shouldExists();
        openButton.shouldHaveLabel("Open");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(1);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(2);

        rows.row(0).cell(0).textShouldHave(firstTableRow);
        rows.row(1).cell(0).textShouldHave(secondTableRow);

        rows.row(clickRow).click();
        openButton.click();
    }
}