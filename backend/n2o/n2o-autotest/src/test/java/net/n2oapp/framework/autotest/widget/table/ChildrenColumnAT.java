package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Автотест атрибута children="expand"
 */
public class ChildrenColumnAT extends AutoTestBase {

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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testTable() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/children_column/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/children_column/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Children column");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.paging().totalElementsShouldBe(4);
        table.columns().rows().shouldHaveSize(10);

        table.columns().rows().columnShouldHaveTexts(0, Arrays.asList("1", "11", "12", "13", "2", "21", "22", "23", "3", "4"));
        table.columns().rows().columnShouldHaveTexts(1, Arrays.asList("test1", "name11", "name12", "name13", "test2", "name21", "name22", "name23", "test3", "test4"));

        table = page.regions().region(1, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.paging().totalElementsShouldBe(4);
        table.columns().rows().shouldHaveSize(4);

        table.columns().rows().columnShouldHaveTexts(0, Arrays.asList("1", "2", "3", "4"));
        table.columns().rows().columnShouldHaveTexts(1, Arrays.asList("test1", "test2", "test3", "test4"));
    }

}
