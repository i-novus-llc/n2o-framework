package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.cell.EditCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
 * Автотест для виджета Таблица. Тестирование row click
 */
class TableRowClickAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testRowClickEnabled() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/row_click/enabled");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/enabled/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/enabled/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/enabled/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);

        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);

        Cells firstRow = table.columns().rows().row(0);
        firstRow.cell(1).shouldHaveText("1");
        Modal modal = N2oSelenide.modal();
        firstRow.click();
        modal.shouldNotExists();

        Cells thirdRow = table.columns().rows().row(2);
        thirdRow.cell(1).shouldHaveText("2");
        thirdRow.click();
        modal.shouldExists();
        modal.close();
    }

    @Test
    void testInteractiveElements() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/row_click/interactive_elements");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/interactive_elements/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/interactive_elements/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/interactive_elements/data.object.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(5);

        Cells firstRow = table.columns().rows().row(0);
        firstRow.cell(1).shouldHaveText("1");

        CheckboxCell selectRowCell = firstRow.cell(0, CheckboxCell.class);
        selectRowCell.shouldBeUnchecked();
        selectRowCell.setChecked(true);
        Alert alert = page.alerts(Alert.Placement.top).alert(0);
        alert.shouldNotExists();

        EditCell cell = firstRow.cell(2, EditCell.class);
        cell.shouldExists();
        InputText input = cell.control(InputText.class);
        input.shouldHaveValue("Иванов П.И.");
        cell.click();
        alert = page.alerts(Alert.Placement.top).alert(0);
        alert.shouldNotExists();
        input.setValue("Иванова П.И.");

        CheckboxCell checkbox = firstRow.cell(3, CheckboxCell.class);
        checkbox.shouldBeChecked();
        checkbox.setChecked(false);
        alert = page.alerts(Alert.Placement.top).alert(0);
        alert.shouldNotExists();

        firstRow.click();
        alert = page.alerts(Alert.Placement.top).alert(0);
        alert.shouldHaveColor(Colors.DANGER);
        alert.shouldHaveText("error");
    }
}
