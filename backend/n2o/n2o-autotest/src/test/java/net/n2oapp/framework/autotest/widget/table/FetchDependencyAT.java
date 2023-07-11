package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
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

public class FetchDependencyAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        setJsonPath("net/n2oapp/framework/autotest/widget/table/fetch_dependency/");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_dependency/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_dependency/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_dependency/test2.query.xml")
        );
    }

    @Test
    void paginationTest() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems regionItems = page.regions().region(0, SimpleRegion.class).content();
        TableWidget tableOne = regionItems.widget(0, TableWidget.class);
        TableWidget tableTwo = regionItems.widget(1, TableWidget.class);

        tableOne.shouldExists();
        tableTwo.shouldExists();
        tableTwo.paging().shouldExists();

        tableOne.columns().rows().row(2).click();
        tableTwo.columns().rows().row(0).cell(0).shouldHaveText("5");

        tableTwo.paging().selectPage("3");
        tableTwo.columns().rows().row(0).cell(0).shouldHaveText("9");

        tableOne.columns().rows().row(3).click();
        tableTwo.columns().rows().shouldHaveSize(0);
        tableTwo.paging().shouldNotExists();

        tableOne.columns().rows().row(1).click();
        tableTwo.paging().shouldHaveActivePage("1");
        tableTwo.columns().rows().row(0).cell(0).shouldHaveText("2");


    }
}
