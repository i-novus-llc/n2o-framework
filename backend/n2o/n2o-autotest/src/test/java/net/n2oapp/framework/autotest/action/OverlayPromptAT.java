package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Автотест //todo
 */

@SpringBootTest(properties = {"n2o.engine.test.classpath=net/n2oapp/framework/autotest/action/overlay_prompt"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    public void scrollableModalTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тест overlay окон");

        Modal modalPage;
        Button openBtn;
        InputText nameControl;

        openBtn = page.single().widget(FormWidget.class).toolbar().topLeft().button("Модалка с подтверждением");
        openBtn.shouldExists();
        openBtn.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Overlay окно");
        nameControl = modalPage.content(SimplePage.class).single().widget(FormWidget.class).fields()
                .field("name").control(InputText.class);
        nameControl.shouldExists();
        nameControl.shouldHaveValue("test1");
        nameControl.val("edited test");
        nameControl.shouldHaveValue("edited test");
        modalPage.close();
        modalPage.shouldExists();
    }

}