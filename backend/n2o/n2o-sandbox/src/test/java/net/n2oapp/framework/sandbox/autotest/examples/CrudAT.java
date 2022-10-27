package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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

@SpringBootTest(properties = {
        "n2o.engine.test.classpath=/examples/crud/",
        "n2o.sandbox.project-id=examples_crud"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrudAT extends SandboxAutotestBase {

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
    }

    @Test
    public void crudTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("CRUD Операции");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(4);

        Button create = table.toolbar().topLeft().button("Создать");
        create.shouldExists();
        Button update = table.toolbar().topLeft().button("Изменить");
        update.shouldExists();
        Button delete = table.toolbar().topLeft().button("Удалить");
        delete.shouldExists();

        create.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("test - Создание");
        Fields modalFields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        InputText inputText = modalFields.field("name").control(InputText.class);
        inputText.shouldExists();
        inputText.val("test-value");
        inputText.shouldHaveValue("test-value");
        Button save = modal.toolbar().bottomRight().button("Сохранить");
        save.shouldExists();
        save.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Данные сохранены");
        rows.shouldHaveSize(5);
        rows.row(0).cell(1).textShouldHave("test-value");

        rows.shouldBeSelected(0);
        update.click();
        modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("test - Изменение");
        InputText inputText1 = modalFields.field("name").control(InputText.class);
        inputText1.shouldExists();
        inputText1.val("change-test-value");
        inputText1.shouldHaveValue("change-test-value");
        Button save1 = modal.toolbar().bottomRight().button("Сохранить");
        save1.shouldExists();
        save1.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Данные сохранены");
        rows.shouldHaveSize(5);
        rows.row(0).cell(1).textShouldHave("change-test-value");

        rows.shouldBeSelected(0);
        delete.click();
        page.dialog("Предупреждение").shouldBeVisible();
        page.dialog("Предупреждение").click("Да");
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Данные сохранены");
        rows.shouldHaveSize(4);
    }

}
