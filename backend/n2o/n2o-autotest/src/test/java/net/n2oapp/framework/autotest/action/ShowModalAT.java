package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест для действия открытия модального окна
 */
public class ShowModalAT extends AutoTestBase {

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
                new CompileInfo("net/n2oapp/framework/autotest/action/modal_scrollable/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/modal_scrollable/test.page.xml"));
    }

    @Test
    public void scrollableModalTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Модальное окно с фиксированной высотой");

        Button openScrollableModal = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть со скроллом");
        openScrollableModal.shouldExists();
        Button openSimpleModal = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть без скролла");
        openSimpleModal.shouldExists();

        openScrollableModal.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        modalPage.shouldBeScrollable();
        Fields fields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.shouldHaveSize(11);
        fields.field("id").control(InputText.class).shouldExists();
        modalPage.scrollDown();
        fields.field("value9").control(InputText.class).shouldExists();
        modalPage.close();


        openSimpleModal.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        modalPage.shouldNotBeScrollable();
        fields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.shouldHaveSize(11);
        fields.field("id").control(InputText.class).shouldExists();
        fields.field("value9").control(InputText.class).shouldExists();
        modalPage.close();
        page.shouldExists();
    }

}