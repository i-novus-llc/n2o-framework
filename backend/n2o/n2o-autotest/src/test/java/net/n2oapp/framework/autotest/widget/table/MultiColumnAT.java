package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.FilterHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.StandardTableHeader;
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

/**
 * Автотест для мульти-столбца таблицы
 */
public class MultiColumnAT extends AutoTestBase {

    private Widgets widgets;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/multi_column/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/multi_column/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        widgets = page.place("single").region(0, SimpleRegion.class).content();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testMultiColumn() {
        TableWidget table = widgets.widget(0, TableWidget.class);
        table.shouldExists();

        TableWidget.Rows rows = table.columns().rows();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(5);
        rows.shouldHaveSize(4);

        StandardTableHeader header1 = headers.header(0);
        StandardTableHeader header2 = headers.header(1);
        StandardTableHeader header3 = headers.header(2);
        StandardTableHeader header4 = headers.header(3);
        StandardTableHeader header5 = headers.header(4);
        // проверка порядка столбцов
        // столбцы первого уровня
        header1.shouldHaveTitle("ID");
        header2.shouldHaveTitle("Info");
        header3.shouldHaveTitle("Birthday");
        // столбцы второго уровня
        header4.shouldHaveTitle("FirstName");
        header5.shouldHaveTitle("LastName");

        // проверка, что все ячейки корректно заполнены
        rows.row(0).cell(0).textShouldHave("1");
        rows.row(0).cell(1).textShouldHave("John");
        rows.row(0).cell(2).textShouldHave("Smith");
        rows.row(0).cell(3).textShouldHave("2018.12.31");
    }

    @Test
    public void testAdvancedMultiColumn() {
        TableWidget table = widgets.widget(1, TableWidget.class);
        table.shouldExists();

        TableWidget.Rows rows = table.columns().rows();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(7);
        rows.shouldHaveSize(4);

        StandardTableHeader header1 = headers.header(0);
        StandardTableHeader header2 = headers.header(1);
        StandardTableHeader header3 = headers.header(2);
        StandardTableHeader header4 = headers.header(3);
        FilterHeader header5 = headers.header(4, FilterHeader.class);
        StandardTableHeader header6 = headers.header(5);
        StandardTableHeader header7 = headers.header(6);
        // проверка порядка столбцов
        // столбцы первого уровня
        header1.shouldHaveTitle("ID");
        header2.shouldHaveTitle("Info");
        header3.shouldHaveTitle("Birthday");
        // столбцы второго уровня
        header4.shouldHaveTitle("Name");
        header5.shouldHaveTitle("Phone");
        // столбцы третьего уровня
        header6.shouldHaveTitle("FirstName");
        header7.shouldHaveTitle("LastName");

        // проверка, что все ячейки корректно заполнены
        rows.row(0).cell(0).textShouldHave("1");
        rows.row(0).cell(1).textShouldHave("John");
        rows.row(0).cell(2).textShouldHave("Smith");
        rows.row(0).cell(3).textShouldHave("11-11-11");
        rows.row(0).cell(4).textShouldHave("2018.12.31");

        // проверка работы фильтруемого столбца внутри мульти-столбца
        header5.openFilterDropdown();
        header5.filterControl(InputText.class).val("2");
        header5.clickSearchButton();
        rows.shouldHaveSize(1);
        rows.row(0).cell(0).textShouldHave("2");
        header5.clickResetButton();
        rows.shouldHaveSize(4);
    }
}
