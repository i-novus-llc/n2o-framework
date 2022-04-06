package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.engine.test.classpath=/examples/modal/",
        "n2o.sandbox.project-id=examples_open_page"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OpenPageAT extends SandboxAutotestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void openPageTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Первая страница");

        TableWidget table = page.widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");
        button.shouldExists();

        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);
        table.columns().rows().shouldHaveSize(4);

        TableWidget.Rows rows = table.columns().rows();
        rows.row(0).click();
        button.click();
        checkNestedPage(page, "1", "test1");

        rows.row(1).click();
        rows.shouldBeSelected(1);
        button.click();
        checkNestedPage(page, "2", "test2");

        rows.row(2).click();
        rows.shouldBeSelected(2);
        button.click();
        checkNestedPage(page, "3", "test3");

        rows.row(3).click();
        rows.shouldBeSelected(3);
        button.click();
        checkNestedPage(page, "4", "test4");
    }

    private void checkNestedPage(SimplePage page, String id, String name) {
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Вторая страница");
        page.breadcrumb().firstTitleShouldHaveText("Первая страница");
        Fields fields = page.widget(FormWidget.class).fields();
        InputText inputText = fields.field("id").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue(id);
        inputText = fields.field("name").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue(name);

        page.breadcrumb().clickLink("Первая страница");
        page.breadcrumb().titleShouldHaveText("Первая страница");
    }

}
