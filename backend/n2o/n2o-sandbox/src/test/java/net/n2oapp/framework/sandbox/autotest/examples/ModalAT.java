package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"n2o.engine.test.classpath=/examples/modal/"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ModalAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("/examples/modal/index.page.xml"),
                new CompileInfo("/examples/modal/modal.page.xml"),
                new CompileInfo("/examples/modal/test.query.xml"));
    }

    @Test

    public void modalTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Модальное окно");

        TableWidget table = page.widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");
        button.shouldExists();

        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);
        table.columns().rows().shouldHaveSize(4);

        TableWidget.Rows rows = table.columns().rows();
        rows.row(0).click();
        button.click();
        checkModal("1", "test1");

        rows.row(1).click();
        rows.shouldBeSelected(1);
        button.click();
        checkModal("2", "test2");

        rows.row(2).click();
        rows.shouldBeSelected(2);
        button.click();
        checkModal("3", "test3");

        rows.row(3).click();
        rows.shouldBeSelected(3);
        button.click();
        checkModal("4", "test4");

    }

    private void checkModal(String id, String name) {
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно");
        Fields fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        InputText inputText = fields.field("id").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue(id);
        inputText = fields.field("name").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue(name);
        modal.close();
    }

}
