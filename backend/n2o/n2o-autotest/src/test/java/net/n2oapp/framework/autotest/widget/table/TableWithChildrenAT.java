package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

import java.util.Arrays;

/**
 * Тестирование древовидной таблицы
 */
class TableWithChildrenAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        setResourcePath("net/n2oapp/framework/autotest/widget/table/with_children");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/with_children/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/with_children/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/with_children/test.query.xml"));
    }

    @Test
    void testTable() {
        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.paging().shouldHaveTotalElements(4);
        table.columns().rows().shouldHaveSize(10);

        table.columns().rows().columnShouldHaveTexts(0, Arrays.asList("1", "11", "12", "13", "2", "21", "22", "23", "3", "4"));
        table.columns().rows().columnShouldHaveTexts(1, Arrays.asList("test1", "name11", "name12", "name13", "test2", "name21", "name22", "name23", "test3", "test4"));

        for (int i = 0; i < 10; i++) {
            if (i == 0 || i == 4) {
                table.columns().rows().row(i).cell(0).shouldBeExpandable();
            } else {
                table.columns().rows().row(i).cell(0).shouldNotBeExpandable();
            }
        }

        table.columns().rows().row(0).cell(0).shouldBeExpanded();
        table.columns().rows().row(4).cell(0).shouldBeExpanded();

        table.columns().rows().row(0).cell(0).expand();
        table.columns().rows().row(0).cell(0).shouldBeCollapsed();
        table.columns().rows().columnShouldHaveTexts(0, Arrays.asList("1", "2", "21", "22", "23", "3", "4"));

        table.columns().rows().row(1).cell(0).shouldBeExpanded();
        table.columns().rows().row(1).cell(0).expand();
        table.columns().rows().row(1).cell(0).shouldBeCollapsed();
        table.columns().rows().columnShouldHaveTexts(0, Arrays.asList("1", "2", "3", "4"));

        table.columns().rows().row(0).cell(0).expand();
        table.columns().rows().row(0).cell(0).shouldBeExpanded();

        table.columns().rows().row(4).cell(0).expand();
        table.columns().rows().row(4).cell(0).shouldBeExpanded();

        table.columns().rows().columnShouldHaveTexts(0, Arrays.asList("1", "11", "12", "13", "2", "21", "22", "23", "3", "4"));
    }

    @Test
    void testRowClickEnabled() {
        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.paging().shouldHaveTotalElements(4);
        table.columns().rows().shouldHaveSize(10);

        Cells parentRow = table.columns().rows().row(0);
        parentRow.cell(1).shouldHaveText("test1");
        parentRow.cell(0).expand();
        table.columns().rows().shouldHaveSize(7);
        Cells childrenRow = table.columns().rows().row(2);
        childrenRow.cell(1).shouldHaveText("name21");
        childrenRow.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.close();
        // проверка на недоступность row click
        childrenRow = table.columns().rows().row(1);
        childrenRow.cell(1).shouldHaveText("test2");
        childrenRow.click();
        modal = N2oSelenide.modal();
        modal.shouldNotExists();
    }
}
