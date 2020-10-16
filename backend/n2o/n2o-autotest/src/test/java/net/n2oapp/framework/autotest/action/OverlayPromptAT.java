package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
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
 * Автотест закрытия overlay окон с подтверждением и без
 */
public class OverlayPromptAT extends AutoTestBase {

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
                new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/test.query.xml"));
    }

    @Test
    public void modalPromptTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест overlay окон");
        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(1);
        rows.shouldBeSelected(0);

        Toolbar tableToolbar = table.toolbar().topLeft();
        Button openBtn = tableToolbar.button("Модалка без подтверждения");
        openBtn.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldHaveTitle("Overlay окно");
        InputText nameControl = modalPage.content(SimplePage.class).widget(FormWidget.class).fields()
                .field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        modalPage.close();
        modalPage.shouldNotExists();

        openBtn = tableToolbar.button("Модалка с подтверждением");
        openBtn.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldHaveTitle("Overlay окно");
        nameControl = modalPage.content(SimplePage.class).widget(FormWidget.class).fields()
                .field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        modalPage.close();
        Selenide.dismiss();
        modalPage.shouldExists();
        modalPage.close();
        Selenide.confirm();
        modalPage.shouldNotExists();
    }

    @Test
    public void drawerPromptTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест overlay окон");
        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(1);
        rows.shouldBeSelected(0);

        Toolbar tableToolbar = table.toolbar().topLeft();
        Button openBtn = tableToolbar.button("Дровер без подтверждения");
        openBtn.click();
        Drawer drawerPage = N2oSelenide.drawer();
        drawerPage.shouldHaveTitle("Overlay окно");
        InputText nameControl = drawerPage.content(SimplePage.class).widget(FormWidget.class).fields()
                .field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        drawerPage.close();
        drawerPage.shouldNotExists();

        openBtn = tableToolbar.button("Дровер с подтверждением");
        openBtn.click();
        drawerPage = N2oSelenide.drawer();
        drawerPage.shouldHaveTitle("Overlay окно");
        nameControl = drawerPage.content(SimplePage.class).widget(FormWidget.class).fields()
                .field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        drawerPage.close();
        Selenide.dismiss();
        drawerPage.shouldExists();
        drawerPage.close();
        Selenide.confirm();
        drawerPage.shouldNotExists();
    }
}