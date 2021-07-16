package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testFilterState() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/open_page/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/open_page/open.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.breadcrumb().titleShouldHaveText("Сохранение фильтров при возврате назад");
        TableWidget table = page.widget(TableWidget.class);

        TableWidget.Rows rows = table.columns().rows();
        InputText nameFilter = table.filters().fields().field("name").control(InputText.class);

        nameFilter.shouldBeEmpty();
        rows.shouldHaveSize(4);
        nameFilter.val("test3");
        table.filters().search();
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).textShouldHave("test3");

        rows.row(0).click();
        StandardPage open = N2oSelenide.page(StandardPage.class);
        open.breadcrumb().titleShouldHaveText("Вторая страница");
        TableWidget openPageTable = open.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        TableWidget.Rows openPageTableRows = openPageTable.columns().rows();
        InputText openPageTypeFilter = openPageTable.filters().fields().field("type").control(InputText.class);

        openPageTypeFilter.shouldBeEmpty();
        openPageTableRows.shouldHaveSize(4);
        openPageTypeFilter.val("2");
        openPageTable.filters().search();
        openPageTableRows.shouldHaveSize(2);

        open.breadcrumb().clickLink("Сохранение фильтров при возврате назад");
        page.breadcrumb().titleShouldHaveText("Сохранение фильтров при возврате назад");

        // preserving filters when going back
        nameFilter.shouldHaveValue("test3");
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).textShouldHave("test3");
        rows.row(0).click();

        // reset filters on return forward
        open.breadcrumb().titleShouldHaveText("Вторая страница");
        openPageTypeFilter.shouldBeEmpty();
        openPageTableRows.shouldHaveSize(4);
    }
}
