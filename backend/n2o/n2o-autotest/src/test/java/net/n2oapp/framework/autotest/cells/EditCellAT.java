package net.n2oapp.framework.autotest.cells;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.EditCell;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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

/**
 * Автотест ячеек таблицы
 */
class EditCellAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void testSimple() {
        setResourcePath("net/n2oapp/framework/autotest/cells/edit/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/edit/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/edit/simple/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/edit/simple/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(5);
        EditCell cell1 = rows.row(0).cell(1, EditCell.class);
        EditCell cell2 = rows.row(0).cell(2, EditCell.class);
        EditCell cell3 = rows.row(0).cell(3, EditCell.class);
        cell1.shouldExists();
        cell2.shouldExists();
        cell3.shouldExists();

        InputText input = cell1.control(InputText.class);
        DateInput date = cell2.control(DateInput.class);
        InputSelect select = cell3.control(InputSelect.class);
        input.shouldExists();
        date.shouldExists();
        select.shouldExists();

        input.shouldHaveValue("Иванов П.И.");
        cell1.click();
        input.setValue("Иванова П.И.");
        input.shouldHaveValue("Иванова П.И.");
        Selenide.refresh();
        input.shouldHaveValue("Иванова П.И.");

        date.shouldHaveValue("11.01.1981");
        cell2.click();
        date.setValue("01.02.1980");
        date.shouldHaveValue("01.02.1980");
        Selenide.refresh();
        date.shouldHaveValue("01.02.1980");

        select.shouldHaveValue("мужской");
        cell3.click();
        select.openPopup();
        select.dropdown().selectItem(1);
        select.pressEnter();
        select.shouldHaveValue("женский");
        Selenide.refresh();
        select.shouldHaveValue("женский");

    }

    @Test
    void testOffline() {
        setResourcePath("net/n2oapp/framework/autotest/cells/edit/offline");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/edit/offline/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/edit/offline/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/edit/offline/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(5);
        EditCell cell1 = rows.row(0).cell(1, EditCell.class);
        EditCell cell2 = rows.row(0).cell(2, EditCell.class);
        EditCell cell3 = rows.row(0).cell(3, EditCell.class);
        cell1.shouldExists();
        cell2.shouldExists();
        cell3.shouldExists();

        InputText input = cell1.control(InputText.class);
        DateInput date = cell2.control(DateInput.class);
        InputSelect select = cell3.control(InputSelect.class);
        input.shouldExists();
        date.shouldExists();
        select.shouldExists();

        input.shouldHaveValue("Иванов П.И.");
        cell1.click();
        input.setValue("Иванова П.И.");
        input.shouldHaveValue("Иванова П.И.");

        date.shouldHaveValue("11.01.1981");
        cell2.click();
        date.setValue("01.02.1980");
        date.shouldHaveValue("01.02.1980");

        select.shouldHaveValue("мужской");
        cell3.click();
        select.openPopup();
        select.dropdown().selectItem(1);
        select.pressEnter();
        select.shouldHaveValue("женский");

        Selenide.refresh();

        input.shouldHaveValue("Иванов П.И.");
        cell1.click();
        input.setValue("Иванова П.И.");
        input.shouldHaveValue("Иванова П.И.");

        date.shouldHaveValue("11.01.1981");
        cell2.click();
        date.setValue("01.02.1980");
        date.shouldHaveValue("01.02.1980");


        select.shouldHaveValue("мужской");
        cell3.click();
        select.openPopup();
        select.dropdown().selectItem(1);
        select.pressEnter();
        select.shouldHaveValue("женский");

        StandardButton button = table.toolbar().topLeft().button("Отправить");
        button.shouldExists();
        button.click();
        Selenide.refresh();

        input.shouldHaveValue("Иванова П.И.");
        date.shouldHaveValue("01.02.1980");
        select.shouldHaveValue("женский");
    }
}
