package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест для групповых операций изменения и удаления
 */
public class BulkOperationAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/bulk_operation/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/bulk_operation/setName.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/bulk_operation/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/bulk_operation/test.query.xml"));
    }

    @Test
    public void bulkOperationTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Bulk Операции");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(3);

        TableWidget.Rows rows = table.columns().rows();
        TextCell name1 = rows.row(0).cell(2, TextCell.class);
        TextCell name2 = rows.row(1).cell(2, TextCell.class);
        name1.textShouldHave("test1");
        name2.textShouldHave("test2");

        // выбор нескольких строк
        CheckboxCell cell1 = rows.row(0).cell(0, CheckboxCell.class);
        CheckboxCell cell2 = rows.row(1).cell(0, CheckboxCell.class);
        cell1.setChecked(true);
        cell1.shouldBeChecked();
        cell2.setChecked(true);
        cell2.shouldBeChecked();

        Button updateManyButton = table.toolbar().topLeft().button("Изменить выбранные");
        updateManyButton.shouldExists();
        updateManyButton.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        Fields fields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        InputText newValue = fields.field("Новое имя").control(InputText.class);
        newValue.shouldExists();
        newValue.val("new name");
        StandardButton saveButton = modalPage.toolbar().bottomRight().button("Сохранить");
        saveButton.shouldExists();
        saveButton.click();
        modalPage.shouldNotExists();
        name1.textShouldHave("new name");
        name2.textShouldHave("new name");

        Button deleteManyButton = table.toolbar().topLeft().button("Удалить выбранные");
        deleteManyButton.shouldExists();
        deleteManyButton.click();
        table.columns().rows().shouldHaveSize(2);
    }

}