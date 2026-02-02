package net.n2oapp.framework.autotest.cells;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.EditCell;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.TextArea;
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

    public static final String FIRST_DATE = "11.01.1981";
    public static final String SECOND_DATE = "01.02.1980";
    public static final String FIRST_NAME = "Иванов П.И.";
    public static final String SECOND_NAME = "Иванова П.И.";
    public static final String MAN = "мужской";
    public static final String WOMAN = "женский";

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void testSimple() {
        Configuration.headless = false;
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
        EditCell cell4 = rows.row(1).cell(4, EditCell.class);
        cell1.shouldExists();
        cell2.shouldExists();
        cell3.shouldExists();
        cell4.shouldExists();

        InputText input = cell1.control(InputText.class);
        DateInput date = cell2.control(DateInput.class);
        InputSelect select = cell3.control(InputSelect.class);
        TextArea textArea = cell4.control(TextArea.class);
        input.shouldExists();
        date.shouldExists();
        select.shouldExists();
        textArea.shouldExists();

        input.shouldHaveValue(FIRST_NAME);
        cell1.click();
        input.setValue(SECOND_NAME);
        input.shouldHaveValue(SECOND_NAME);
        table.toolbar().topLeft().button("Установить Test").click();
        input.shouldHaveValue("Test");
        Selenide.refresh();
        input.shouldHaveValue("Test");

        date.shouldHaveValue(FIRST_DATE);
        cell2.click();
        date.setValue(SECOND_DATE);
        date.shouldHaveValue(SECOND_DATE);
        Selenide.refresh();
        date.shouldHaveValue(SECOND_DATE);

        cell2.click();
        date.clear();
        date.shouldBeEmpty();
        Selenide.refresh();
        date.shouldBeEmpty();

        select.shouldHaveValue(MAN);
        cell3.click();
        select.openPopup();
        select.dropdown().selectItem(1);
        select.pressEnter();
        select.shouldHaveValue(WOMAN);
        Selenide.refresh();
        select.shouldHaveValue(WOMAN);

        textArea.shouldHaveValue("");
        cell4.click();
        textArea.setValue("1\n2\n3\n4\n5\n6\n7");
        textArea.shouldHaveValue("1\n2\n3\n4\n5\n6\n7");
        Selenide.refresh();
        textArea.shouldBeVisible();
        textArea.shouldHaveValue("1\n2\n3\n4\n5\n6\n7");
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

        input.shouldHaveValue(FIRST_NAME);
        cell1.click();
        input.setValue(SECOND_NAME);
        input.shouldHaveValue(SECOND_NAME);

        date.shouldHaveValue(FIRST_DATE);
        cell2.click();
        date.setValue(SECOND_DATE);
        date.shouldHaveValue(SECOND_DATE);

        table.toolbar().topLeft().button("Click").click();

        select.shouldHaveValue(MAN);
        cell3.click();
        select.openPopup();
        select.dropdown().selectItem(1);
        select.pressEnter();
        select.shouldHaveValue(WOMAN);

        Selenide.refresh();

        input.shouldHaveValue(FIRST_NAME);
        cell1.click();
        input.setValue(SECOND_NAME);
        input.shouldHaveValue(SECOND_NAME);

        date.shouldHaveValue(FIRST_DATE);
        cell2.click();
        date.setValue(SECOND_DATE);
        date.shouldHaveValue(SECOND_DATE);

        table.toolbar().topLeft().button("Click").click();

        select.shouldHaveValue(MAN);
        cell3.click();
        select.openPopup();
        select.dropdown().selectItem(1);
        select.pressEnter();
        select.shouldHaveValue(WOMAN);

        StandardButton button = table.toolbar().topLeft().button("Отправить");
        button.shouldExists();
        button.click();
        Selenide.refresh();

        input.shouldHaveValue(SECOND_NAME);
        date.shouldHaveValue(SECOND_DATE);
        select.shouldHaveValue(WOMAN);
    }
}
