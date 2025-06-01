package net.n2oapp.framework.autotest.validation.dialog;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
class DialogValidationAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testDialog() {
        setResourcePath("net/n2oapp/framework/autotest/validation/dialog");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/dialog/test.object.xml"));

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
        StandardButton modalSaveBtn = modalForm.toolbar().topLeft().button("Сохранить");
        InputText name = modalForm.fields().field("name").control(InputText.class);
        InputText age = modalForm.fields().field("age").control(InputText.class);

        // save with correct data (without dialog)
        name.click();
        name.setValue("Mark");
        age.click();
        age.setValue("20");
        modalSaveBtn.click();

        modal.shouldNotExists();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Данные сохранены");
        tableRows.shouldHaveSize(3);
        tableRows.row(0).cell(0).shouldHaveText("3");
        tableRows.row(0).cell(1).shouldHaveText("Mark");
        tableRows.row(0).cell(2).shouldHaveText("20");

        // save without name (calling dialog 'nameCheck')
        create.click();
        modal.shouldExists();

        age.click();
        age.setValue("25");
        modalSaveBtn.click();

        Page.Dialog dialog = page.dialog("Вы не заполнили имя.");
        dialog.shouldHaveText("Заполнить его значением по умолчанию?");

        Button closeBtn = dialog.button("Нет");
        closeBtn.shouldExists();
        closeBtn.shouldBeVisible();
        closeBtn.click();

        modalSaveBtn.click();

        Button agreeBnt = dialog.button("Да");
        agreeBnt.click();

        modal.shouldNotExists();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Данные сохранены");
        tableRows.shouldHaveSize(4);
        tableRows.row(0).cell(0).shouldHaveText("4");
        tableRows.row(0).cell(1).shouldHaveText("default");
        tableRows.row(0).cell(2).shouldHaveText("25");

        // save without age (calling dialog 'ageCheck')
        create.click();
        modal.shouldExists();

        name.click();
        name.setValue("Ann");
        modalSaveBtn.click();

        dialog = page.dialog("Вы не заполнили возраст.");
        dialog.shouldBeVisible();
        dialog.shouldHaveText("Заполнить его значением по умолчанию?");

        closeBtn = dialog.button("Нет");
        closeBtn.shouldExists();
        closeBtn.shouldBeEnabled();
        closeBtn.click();

        modalSaveBtn.click();

        agreeBnt = dialog.button("Да");
        agreeBnt.click();

        modal.shouldNotExists();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Данные сохранены");
        tableRows.shouldHaveSize(5);
        tableRows.row(0).cell(0).shouldHaveText("5");
        tableRows.row(0).cell(1).shouldHaveText("Ann");
        tableRows.row(0).cell(2).shouldHaveText("0");
    }
}
