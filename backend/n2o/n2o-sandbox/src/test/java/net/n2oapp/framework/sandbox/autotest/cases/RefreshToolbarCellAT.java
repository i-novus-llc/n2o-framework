package net.n2oapp.framework.sandbox.autotest.cases;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.engine.test.classpath=/cases/7.11/refresh_toolbar_cell",
        "n2o.sandbox.project-id=cases_7.11_refresh_toolbar_cell"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RefreshToolbarCellAT extends SandboxAutotestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"));
    }

    @Test
    public void checkboxTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Динамическое обновление ячеек таблицы");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(7);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(4);

        table.columns().rows().row(0).cell(0).textShouldHave("test1");
        table.columns().rows().row(0).cell(1, ToolbarCell.class).toolbar().button("enabled for 'test1'").shouldBeEnabled();
        table.columns().rows().row(0).cell(2, CheckboxCell.class).shouldBeEnabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().click();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().shouldHaveItems(3);
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("enabled for 'test1'").shouldBeEnabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("always disabled").shouldBeDisabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("always enabled").shouldBeEnabled();

        table.columns().rows().row(0).cell(4, ToolbarCell.class).toolbar().button("visible for 'test1'").shouldExists();
        table.columns().rows().row(0).cell(5, CheckboxCell.class).shouldExists();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().click();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().shouldHaveItems(2);
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().menuItem("visible for 'test1'").shouldExists();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().menuItem("always visible").shouldExists();

        changeName(table.toolbar().topLeft().button("update"), "test22");

        table.columns().rows().row(0).cell(0).textShouldHave("test22");
        table.columns().rows().row(0).cell(1, ToolbarCell.class).toolbar().button("enabled for 'test1'").shouldBeDisabled();
        table.columns().rows().row(0).cell(2, CheckboxCell.class).shouldBeDisabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().click();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("enabled for 'test1'").shouldBeDisabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("always disabled").shouldBeDisabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("always enabled").shouldBeEnabled();

        table.columns().rows().row(0).cell(4, ToolbarCell.class).toolbar().shouldBeEmpty();
        table.columns().rows().row(0).cell(5, CheckboxCell.class).shouldNotExists();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().click();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().shouldHaveItems(1);
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().menuItem("always visible").shouldExists();

        changeName(table.toolbar().topLeft().button("update"), "test1");

        table.columns().rows().row(0).cell(0).textShouldHave("test1");
        table.columns().rows().row(0).cell(1, ToolbarCell.class).toolbar().button("enabled for 'test1'").shouldBeEnabled();
        table.columns().rows().row(0).cell(2, CheckboxCell.class).shouldBeEnabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().click();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("enabled for 'test1'").shouldBeEnabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("always disabled").shouldBeDisabled();
        table.columns().rows().row(0).cell(3, ToolbarCell.class).toolbar().dropdown().menuItem("always enabled").shouldBeEnabled();

        table.columns().rows().row(0).cell(4, ToolbarCell.class).toolbar().button("visible for 'test1'").shouldExists();
        table.columns().rows().row(0).cell(5, CheckboxCell.class).shouldExists();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().click();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().shouldHaveItems(2);
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().menuItem("visible for 'test1'").shouldExists();
        table.columns().rows().row(0).cell(6, ToolbarCell.class).toolbar().dropdown().menuItem("always visible").shouldExists();
    }

    private void changeName(Button update, String newName) {
        update.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("update");

        StandardField field = modal.content(SimplePage.class).widget(FormWidget.class).fields().field("name");
        field.control(InputText.class).val(newName);

        Button save = modal.toolbar().bottomRight().button("Сохранить");
        save.shouldExists();
        save.click();
    }

}
