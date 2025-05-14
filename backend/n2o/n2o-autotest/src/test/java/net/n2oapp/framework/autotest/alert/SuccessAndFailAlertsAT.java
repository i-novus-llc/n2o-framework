package net.n2oapp.framework.autotest.alert;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование текста и заголовка сообщения для success/fail выполненных операций
 */
class SuccessAndFailAlertsAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/operation/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/operation/test.object.xml"));
    }

    @Test
    void testOperation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText inputText = page.widget(FormWidget.class).fields().field("Число").control(InputText.class);
        inputText.click();
        inputText.setValue("2342");

        StandardButton button = page.widget(FormWidget.class).toolbar().bottomLeft().button("Отправить");
        button.click();

        Alert alert = page.alerts(Alert.PlacementEnum.top).alert(0);
        alert.shouldHaveColor(ColorsEnum.SUCCESS);
        alert.shouldHaveTitle("Заголовок успеха. Введенные данные: 2342");
        alert.shouldHaveText("Текст успеха. Введенные данные: 2342");

        inputText.click();
        inputText.setValue("строка");
        button.click();
        alert.shouldHaveColor(ColorsEnum.DANGER);
        alert.shouldHaveTitle("Заголовок ошибки. Введенные данные: строка");
        alert.shouldHaveText("Текст ошибки. Введенные данные: строка");
    }

    @Test
    void testValidation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText inputText = page.widget(FormWidget.class).fields().field("Число").control(InputText.class);
        inputText.click();
        inputText.setValue("1223");

        StandardButton button = page.widget(FormWidget.class).toolbar().bottomLeft().button("Отправить");
        button.click();

        Alert alert = page.alerts(Alert.PlacementEnum.top).alert(0);
        alert.shouldHaveColor(ColorsEnum.DANGER);
        alert.shouldHaveTitle("Заголовок валидации. Введенные данные: 1223");
        alert.shouldHaveText("Сообщение валидации. Введенные данные: 1223");
    }
}
