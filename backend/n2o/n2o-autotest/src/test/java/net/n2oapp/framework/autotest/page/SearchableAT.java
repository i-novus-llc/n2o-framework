package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.api.component.page.SearchablePage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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

@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.engine.test.classpath=/uxcomponents/pages/search"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchableAT extends AutoTestBase {

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
                new CompileInfo("/uxcomponents/pages/search/index.page.xml"),
                new CompileInfo("/uxcomponents/pages/search/search.query.xml"));
    }

    @Test
    public void crudTest() {
        SearchablePage page = open(SearchablePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница с поисковой строкой");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().shouldHaveSize(10);

        page.searchBar().search("test");
        table.columns().rows().shouldHaveSize(0);

        page.searchBar().clear();
        table.columns().rows().shouldHaveSize(10);

        page.searchBar().search("Иванов");

        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("Иванов");
    }
}