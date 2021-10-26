package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
 * Автотест закрытия overlay окон с подтверждением и без
 */
public class OverlayPromptAT extends AutoTestBase {

    private Toolbar tableToolbar;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget table = page.widget(TableWidget.class);
        tableToolbar = table.toolbar().topLeft();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/test.query.xml"));
    }

    @Test
    public void modalPromptTest() {
        Button openBtn = tableToolbar.button("Модалка без подтверждения");
        openBtn.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldHaveTitle("Overlay окно");
        // из-за сложности реализации регионов важно проверять именно в <page>, a не <simple-page>
        InputText nameControl = modalPage.content(StandardPage.class).regions().region(0, SimpleRegion.class)
                .content().widget(FormWidget.class).fields().field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        modalPage.close();
        modalPage.shouldNotExists();

        openBtn = tableToolbar.button("Модалка с подтверждением");
        openBtn.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldHaveTitle("Overlay окно");
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
        Button openBtn = tableToolbar.button("Дровер без подтверждения");
        openBtn.click();
        Drawer drawerPage = N2oSelenide.drawer();
        drawerPage.shouldHaveTitle("Overlay окно");
        InputText nameControl = drawerPage.content(StandardPage.class).regions().region(0, SimpleRegion.class)
                .content().widget(FormWidget.class).fields().field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        drawerPage.close();
        drawerPage.shouldNotExists();

        openBtn = tableToolbar.button("Дровер с подтверждением");
        openBtn.click();
        drawerPage = N2oSelenide.drawer();
        drawerPage.shouldHaveTitle("Overlay окно");
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

    @Test
    public void openPagePromptTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/overlay_prompt/pageWithPrompt.page.xml"));
        Button openBtn = tableToolbar.button("OpenPage без подтверждения");
        openBtn.click();
        StandardPage openPage = N2oSelenide.page(StandardPage.class);
        Page.Breadcrumb breadcrumb = openPage.breadcrumb();
        breadcrumb.titleShouldHaveText("Overlay окно");
        InputText nameControl = openPage.regions().region(0, SimpleRegion.class)
                .content().widget(FormWidget.class).fields().field("name").control(InputText.class);
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        breadcrumb.parentTitleShouldHaveText("Тест overlay окон");
        breadcrumb.clickLink("Тест overlay окон");
        breadcrumb.titleShouldHaveText("Тест overlay окон");

        openBtn = tableToolbar.button("OpenPage с подтверждением");
        openBtn.click();
        breadcrumb.titleShouldHaveText("Overlay окно");
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited");
        nameControl.shouldHaveValue("edited");
        breadcrumb.parentTitleShouldHaveText("Тест overlay окон");
        breadcrumb.clickLink("Тест overlay окон");
        Selenide.dismiss();
        breadcrumb.titleShouldHaveText("Overlay окно");
        breadcrumb.clickLink("Тест overlay окон");
        Selenide.confirm();
        breadcrumb.titleShouldHaveText("Тест overlay окон");
    }
}