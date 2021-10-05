package net.n2oapp.framework.autotest.validation.dialog;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест для диалога подтверждения действия
 */
public class ValidationDialogAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/test.object.xml"));
    }

    @Test
    public void testDialog() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        TableWidget.Rows tableRows = table.columns().rows();
        tableRows.shouldHaveSize(2);
        StandardButton create = table.toolbar().topLeft().button("Create");
        create.shouldExists();
        create.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        FormWidget modalForm = modal.content(SimplePage.class).widget(FormWidget.class);
        StandardButton modalSaveBtn = modal.toolbar().bottomRight().button("Save");
        InputText name = modalForm.fields().field("name").control(InputText.class);
        InputText age = modalForm.fields().field("age").control(InputText.class);

        // save with correct data (without dialog)
        name.val("Mark");
        age.val("20");
        modalSaveBtn.click();
        modal.shouldNotExists();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        tableRows.shouldHaveSize(3);
        tableRows.row(2).cell(0).textShouldHave("3");
        tableRows.row(2).cell(1).textShouldHave("Mark");
        tableRows.row(2).cell(2).textShouldHave("20");

        // save without name (calling dialog 'nameCheck')
        create.click();
        modal.shouldExists();
        age.val("25");
        modalSaveBtn.click();
        Page.Dialog dialog = page.dialog("Вы не заполнили имя.");
        //todo добавить проверку на тело диалога
        dialog.click("Close");
        modalSaveBtn.click();
        dialog.click("Yes");
        modal.shouldNotExists();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        tableRows.shouldHaveSize(4);
        tableRows.row(3).cell(0).textShouldHave("4");
        tableRows.row(3).cell(1).textShouldHave("default");
        tableRows.row(3).cell(2).textShouldHave("25");

        // save without age (calling dialog 'ageCheck')
        create.click();
        modal.shouldExists();
        name.val("Ann");
        modalSaveBtn.click();
        dialog = page.dialog("Вы не заполнили возраст.");
        //todo добавить проверку на тело диалога
        dialog.click("Close");
        modalSaveBtn.click();
        dialog.click("Yes");
        modal.shouldNotExists();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        tableRows.shouldHaveSize(5);
        tableRows.row(4).cell(0).textShouldHave("5");
        tableRows.row(4).cell(1).textShouldHave("Ann");
        tableRows.row(4).cell(2).textShouldHave("0");
    }
}
