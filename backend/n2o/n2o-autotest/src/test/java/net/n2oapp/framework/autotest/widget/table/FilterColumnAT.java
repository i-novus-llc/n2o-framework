package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Автотест для фильтруемого столбца таблицы
 */
public class FilterColumnAT extends AutoTestBase {
    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_column/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_column/table.widget.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_column/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testFilterColumn() {
        TableWidget table = page.single().widget(TableWidget.class);
        table.shouldExists();

        TableWidget.Rows rows = table.columns().rows();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(4);

        StandardTableHeader header1 = headers.header(0);
        FilterHeader header2 = headers.header(1, FilterHeader.class);
        FilterHeader header3 = headers.header(2, FilterHeader.class);
        FilterHeader header4 = headers.header(3, FilterHeader.class);
        header1.shouldHaveTitle("id");
        header2.shouldHaveTitle("name");
        header3.shouldHaveTitle("type");
        header4.shouldHaveTitle("birthday");

        rows.shouldHaveSize(4);

        // проверка фильтрации через текстовое поле
        header2.openFilterDropdown();
        InputText header2Input = header2.filterControl(InputText.class);
        header2Input.val("1");
        header2.clickSearchButton();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).textShouldHave("1");
        rows.row(1).cell(0).textShouldHave("2");
        header2.clickResetButton();
        rows.shouldHaveSize(4);

        // проверка фильтрации через списковое поле
        header3.openFilterDropdown();
        Select header3Input = header3.filterControl(Select.class);
        header3Input.select(0);
        header3.clickSearchButton();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).textShouldHave("1");
        rows.row(1).cell(0).textShouldHave("4");
        header3.clickResetButton();
        rows.shouldHaveSize(4);

        // проверка фильтрации через интервальное поле
        header4.openFilterDropdown();
        DateInterval header4Input = header4.filterControl(DateInterval.class);
        header4Input.beginVal("01.01.2019");
        header4Input.endVal("01.01.2021");
        // закрытие / открытие фильтра, т.к. календарь закрывает кнопки
        header4.openFilterDropdown();
        header4.openFilterDropdown();
        header4.clickSearchButton();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).textShouldHave("2");
        rows.row(1).cell(0).textShouldHave("3");
        header4.clickResetButton();
        rows.shouldHaveSize(4);

        // два фильтра одновременно
        header2.openFilterDropdown();
        header2Input.val("1");
        header2.clickSearchButton();
        header3.openFilterDropdown();
        header3Input.select(0);
        header3.clickSearchButton();
        rows.shouldHaveSize(1);
        rows.row(0).cell(0).textShouldHave("1");
        // должен сбрасываться только один фильтр
        header2.openFilterDropdown();
        header2.clickResetButton();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).textShouldHave("1");
        rows.row(1).cell(0).textShouldHave("4");
    }
}
