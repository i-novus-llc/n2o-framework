package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.api.component.page.SearchablePage;
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
 * Автотест для проверки страницы с поисковой строкой
 */
class SearchableAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        setResourcePath("net/n2oapp/framework/autotest/page/searchable");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/page/searchable/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/searchable/search.query.xml"));
    }

    @Test
    void crudTest() {
        SearchablePage page = open(SearchablePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница с поисковой строкой");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().shouldHaveSize(10);

        page.searchBar().search("test");
        table.columns().rows().shouldHaveSize(0);

        page.searchBar().clear();
        table.columns().rows().shouldHaveSize(10);

        page.searchBar().search("Иванов");

        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).shouldHaveText("Иванов И.И.");
    }
}