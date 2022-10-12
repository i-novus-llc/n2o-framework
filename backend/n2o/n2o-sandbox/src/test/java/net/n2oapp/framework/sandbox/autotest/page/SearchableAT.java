package net.n2oapp.framework.sandbox.autotest.page;

import net.n2oapp.framework.autotest.api.component.page.SearchablePage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "n2o.engine.test.classpath=/uxcomponents/pages/search",
        "n2o.sandbox.project-id=uxcomponents_pages_search"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchableAT extends SandboxAutotestBase {

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
    }

    @Test
    public void crudTest() {
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
        table.columns().rows().row(0).cell(0).textShouldHave("Иванов");
    }
}
