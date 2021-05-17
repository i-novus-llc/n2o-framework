package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
 * Автотест для обновления после закрытия модального окна
 */
public class CloseWithRefreshAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/test.object.xml"));
    }

    @Test
    public void testCloseRefresh() {
        SimplePage page = open(SimplePage.class);
        page.breadcrumb().titleShouldHaveText("Close refresh test");
        page.shouldExists();

        TableWidget.Rows rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);
        rows.row(1).click();
        rows.shouldBeSelected(1);
        rows.row(1).cell(1).textShouldHave("test2");

        StandardButton openBtn = page.widget(TableWidget.class).toolbar().topLeft().button("Open");
        openBtn.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();

        FormWidget modalForm = modalPage.content(SimplePage.class).widget(FormWidget.class);
        StandardButton updateBtn = modalForm.toolbar().bottomLeft().button("Update");
        StandardButton closeBtn = modalForm.toolbar().bottomLeft().button("Close");
        InputText inputText = modalForm.fields().field("name").control(InputText.class);

        inputText.shouldHaveValue("test2");
        inputText.val("change1");
        inputText.shouldHaveValue("change1");
        updateBtn.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        closeBtn.click();

        rows.shouldBeSelected(1);
        rows.row(1).cell(1).textShouldHave("change1");

        // refresh after close modal (by click on close icon) does not occur
        //openBtn.click();
        //modalPage.shouldExists();
        //inputText.shouldHaveValue("change1");
        //inputText.val("change2");
        //inputText.shouldHaveValue("change2");
        //updateBtn.click();
        //page.alerts().alert(0).shouldHaveText("Данные сохранены");
        //modalPage.close();

        //rows.row(1).cell(1).textShouldHave("change1");
        //Selenide.refresh();
        //rows.row(1).cell(1).textShouldHave("change2");
    }
}
