package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест компонента ввода текста
 */
public class InputTextAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"));
    }

    @Test
    public void testInputText() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        InputText input = page.widget(FormWidget.class).fields().field("InputText")
                .control(InputText.class);
        input.shouldExists();

        input.shouldHavePlaceholder("Введите текст");
        input.shouldBeEmpty();
        input.click();
        input.setValue("test-value");
        input.shouldHaveValue("test-value");
        // превышение указанной длины
        input.click();
        input.setValue("test1test2test3");
        input.shouldHaveValue("test1test2");
        // проверка меры измерения
        input.shouldHaveMeasure();
        input.shouldHaveMeasureText("шт.");
    }

    @Test
    public void testInputNumber() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText input = page.widget(FormWidget.class).fields().field("InputNumber")
                .control(InputText.class);
        input.shouldExists();

        input.shouldHaveValue("5");
        input.click();
        input.setValue("10");
        input.shouldHaveValue("10");
        // проверка работы кнопок (+step, -step)
        input.clickPlusStepButton();
        input.shouldHaveValue("12");
        input.clickMinusStepButton();
        input.shouldHaveValue("10");
        // проверка, что значение, измененное кнопками не выходит за границу
        input.click();
        input.setValue("99");
        input.clickPlusStepButton();
        input.shouldHaveValue("99");
        input.click();
        input.setValue("-99");
        input.clickMinusStepButton();
        input.shouldHaveValue("-99");
        // проверка меры измерения
        input.shouldHaveMeasure();
        input.shouldHaveMeasureText("cm");
    }

    @Test
    public void testInputFloat() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"));
        SimplePage page = open(SimplePage.class);

        page.shouldExists();
        InputText input = page.widget(FormWidget.class).fields().field("InputFloat")
                .control(InputText.class);
        input.shouldExists();

        input.click();
        input.setValue("7.7");
        input.shouldHaveValue("7.7");
        // проверка работы кнопок (+step, -step)
        input.clickPlusStepButton();
        input.shouldHaveValue("8.2");
        input.clickMinusStepButton();
        input.shouldHaveValue("7.7");
        // проверка, что значение, измененное кнопками не выходит за границу
        input.click();
        input.setValue("9.9");
        input.clickPlusStepButton();
        input.shouldHaveValue("9.9");
        input.click();
        input.setValue("-9.9");
        input.clickMinusStepButton();
        input.shouldHaveValue("-9.9");
    }

    @Test
    public void testInputNumericPrecision() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText input = page.widget(FormWidget.class).fields().field("Дробная часть длины 4")
                .control(InputText.class);
        input.shouldExists();
        input.shouldHaveValue("1.3333");
        input.click();
        input.setValue("9.99999999");
        input.shouldHaveValue("9.9999");

        input = page.widget(FormWidget.class).fields().field("integer")
                .control(InputText.class);
        input.shouldExists();
        input.shouldHaveValue("5");
        input.click();
        input.setValue("9.99");
        input.shouldHaveValue("999");
    }

    @Test
    public void testArrowsDifIntervals() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_text/min_max/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText input1 = page.widget(FormWidget.class).fields().field("1. max=-2 min=-5")
                .control(InputText.class);
        InputText input2 = page.widget(FormWidget.class).fields().field("2. max=5 min=2")
                .control(InputText.class);
        InputText input3 = page.widget(FormWidget.class).fields().field("3. max=5 domain=numeric")
                .control(InputText.class);
        InputText input4 = page.widget(FormWidget.class).fields().field("4. max=5")
                .control(InputText.class);
        InputText input5 = page.widget(FormWidget.class).fields().field("5. max=-5")
                .control(InputText.class);
        InputText input6 = page.widget(FormWidget.class).fields().field("6. min=5")
                .control(InputText.class);
        InputText input7 = page.widget(FormWidget.class).fields().field("7. min=-5")
                .control(InputText.class);
        InputText max0 = page.widget(FormWidget.class).fields().field("8. max=0")
                .control(InputText.class);
        InputText min0 = page.widget(FormWidget.class).fields().field("9. min=0")
                .control(InputText.class);
        InputText noLimits = page.widget(FormWidget.class).fields().field("10. нет ограничений")
                .control(InputText.class);

        input1.clickPlusStepButton();
        input1.shouldHaveValue("-2");
        input1.clickPlusStepButton();
        input1.shouldHaveValue("-2");

        input2.clickPlusStepButton();
        input2.shouldHaveValue("2");
        input2.clickPlusStepButton();
        input2.shouldHaveValue("3");

        input3.clickPlusStepButton();
        input3.shouldHaveValue("0.01");
        input3.clickPlusStepButton();
        input3.shouldHaveValue("0.02");

        input4.clickPlusStepButton();
        input4.shouldHaveValue("1");
        input4.clickPlusStepButton();
        input4.shouldHaveValue("2");

        input5.clickPlusStepButton();
        input5.shouldHaveValue("-5");
        input5.clickPlusStepButton();
        input5.shouldHaveValue("-5");

        input6.clickPlusStepButton();
        input6.shouldHaveValue("5");
        input6.clickPlusStepButton();
        input6.shouldHaveValue("6");

        input7.clickPlusStepButton();
        input7.shouldHaveValue("1");
        input7.clickPlusStepButton();
        input7.shouldHaveValue("2");

        max0.clickPlusStepButton();
        max0.shouldHaveValue("0");
        max0.clickPlusStepButton();
        max0.shouldHaveValue("0");

        min0.clickPlusStepButton();
        min0.shouldHaveValue("1");
        min0.clickPlusStepButton();
        min0.shouldHaveValue("2");

        noLimits.clickPlusStepButton();
        noLimits.shouldHaveValue("1");
        noLimits.clickPlusStepButton();
        noLimits.shouldHaveValue("2");

        Selenide.refresh();

        input1.clickMinusStepButton();
        input1.shouldHaveValue("-2");
        input1.clickMinusStepButton();
        input1.shouldHaveValue("-3");

        input2.clickMinusStepButton();
        input2.shouldHaveValue("2");
        input2.clickMinusStepButton();
        input2.shouldHaveValue("2");

        input3.clickMinusStepButton();
        input3.shouldHaveValue("-0.01");
        input3.clickMinusStepButton();
        input3.shouldHaveValue("-0.02");

        input4.clickMinusStepButton();
        input4.shouldHaveValue("-1");
        input4.clickMinusStepButton();
        input4.shouldHaveValue("-2");

        input5.clickMinusStepButton();
        input5.shouldHaveValue("-5");
        input5.clickMinusStepButton();
        input5.shouldHaveValue("-6");

        input6.clickMinusStepButton();
        input6.shouldHaveValue("5");
        input6.clickMinusStepButton();
        input6.shouldHaveValue("5");

        input7.clickMinusStepButton();
        input7.shouldHaveValue("-1");
        input7.clickMinusStepButton();
        input7.shouldHaveValue("-2");

        max0.clickMinusStepButton();
        max0.shouldHaveValue("-1");
        max0.clickMinusStepButton();
        max0.shouldHaveValue("-2");

        min0.clickMinusStepButton();
        min0.shouldHaveValue("0");
        min0.clickMinusStepButton();
        min0.shouldHaveValue("0");

        noLimits.clickMinusStepButton();
        noLimits.shouldHaveValue("-1");
        noLimits.clickMinusStepButton();
        noLimits.shouldHaveValue("-2");
    }
}