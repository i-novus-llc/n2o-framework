package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест для проверки проброса значения обратно из модального окна
 */
public class DataBackFromModalAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/data_back_from_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/data_back_from_modal/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/data_back_from_modal/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/data_back_from_modal/test.query.xml"));
    }

    @Test
    public void dataBackFromModalTest() {
        SimplePage page = open(SimplePage.class);
        openAndCheckPage(page, 0);
        page.widget(FormWidget.class).fields().field("Выборка текущего виджета").control(InputText.class).shouldBeEmpty();
        openAndCheckModal(page);

        page = open(SimplePage.class);
        openAndCheckPage(page, 1);
        page.widget(FormWidget.class).fields().field("Выборка текущего виджета").control(InputText.class).shouldHaveValue("test1");
        openAndCheckModal(page);

    }

    private void openAndCheckPage(SimplePage page, Integer buttonNumber) {
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Проброс значений обратно из модального окна");
        page.widget(FormWidget.class).toolbar().bottomLeft().button(buttonNumber, StandardButton.class).shouldExists();
        page.widget(FormWidget.class).toolbar().bottomLeft().button(buttonNumber, StandardButton.class).click();
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница с/без выборки");
        StandardField testField = page.widget(FormWidget.class).fields().field("Поле с данными, введенными в модальном окне");
        testField.control(InputText.class).shouldBeDisabled();
        testField.control(InputText.class).shouldBeEmpty();
        page.widget(FormWidget.class).fields().field("Выборка текущего виджета").control(InputText.class).shouldBeDisabled();
    }

    private void openAndCheckModal(SimplePage page) {
        StandardField testField = page.widget(FormWidget.class).fields().field("Поле с данными, введенными в модальном окне");
        testField.toolbar().button("Ввести данные в модальном окне").shouldBeEnabled();
        testField.toolbar().button("Ввести данные в модальном окне").click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("Модальная страница с прокинутыми значениями");
        StandardField inputField = modal.content(SimplePage.class).widget(FormWidget.class)
                .fields().field("Поле ввода, данные из которого должны отобразиться на странице");
        inputField.shouldExists();
        inputField.control(InputText.class).val("test message");
        inputField.control(InputText.class).shouldHaveValue("test message");
        modal.toolbar().bottomLeft().button("Сохранить").click();
        testField.control(InputText.class).shouldHaveValue("test message");
    }
}
