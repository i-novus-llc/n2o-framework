package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
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
 * Автотест столбца чекбоксов таблицы
 */
public class TableCheckboxesAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/checkboxes/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/checkboxes/test.query.xml"));
        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testCheckboxes() {
        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(3);

        TableWidget.Rows rows = table.columns().rows();
        Paging paging = table.paging();
        rows.shouldHaveSize(10);
        paging.totalElementsShouldBe(12);
        paging.activePageShouldBe("1");

        CheckboxCell cell1 = rows.row(0).cell(0, CheckboxCell.class);
        CheckboxCell cell2 = rows.row(1).cell(0, CheckboxCell.class);
        CheckboxCell cell3 = rows.row(2).cell(0, CheckboxCell.class);
        CheckboxCell cell4 = rows.row(3).cell(0, CheckboxCell.class);
        // выбор нескольких строк
        cell1.shouldBeUnchecked();
        cell2.setChecked(true);
        cell2.shouldBeChecked();
        cell3.setChecked(true);
        cell3.shouldBeChecked();
        cell4.shouldBeUnchecked();
        // переходим на вторую страницу
        paging.selectPage("2");
        paging.activePageShouldBe("2");
        rows.shouldHaveSize(2);
        CheckboxCell cell11 = rows.row(0).cell(0, CheckboxCell.class);
        CheckboxCell cell12 = rows.row(1).cell(0, CheckboxCell.class);
        cell11.shouldBeUnchecked();
        cell12.shouldBeUnchecked();
        // выбираем все строки
        table.columns().headers().header(0).click();
        paging.selectPage("1");
        paging.activePageShouldBe("1");
        // переходим на первую страницу
        // вторая и третья строка должны быть выбраны, остальные - нет
        cell1.shouldBeUnchecked();
        cell2.shouldBeChecked();
        cell3.shouldBeChecked();
        cell4.shouldBeUnchecked();
        // переходим на вторую страницу
        // все строки должны быть выбраны
        paging.selectPage("2");
        cell11.shouldBeChecked();
        cell12.shouldBeChecked();
        // отмена выбора всех строк
        table.columns().headers().header(0).click();
        cell11.shouldBeUnchecked();
        cell12.shouldBeUnchecked();
        paging.selectPage("1");
        paging.selectPage("2");
        cell11.shouldBeUnchecked();
        cell12.shouldBeUnchecked();
    }
}
