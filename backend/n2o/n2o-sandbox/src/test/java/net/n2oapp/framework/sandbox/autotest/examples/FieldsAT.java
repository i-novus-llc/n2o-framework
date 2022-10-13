package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "n2o.engine.test.classpath=/examples/fields/",
        "n2o.sandbox.project-id=examples_fields"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FieldsAT extends SandboxAutotestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Поля");
        page.widget(FormWidget.class).fieldsets().fieldset(2).shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
    }

    @Test
    public void testInputTextField() {
        InputText inputText = page.widget(FormWidget.class).fields().field("Ввод текста")
                .control(InputText.class);
        inputText.shouldExists();
        inputText.shouldBeEmpty();
        inputText.val("text");
        inputText.shouldHaveValue("text");
        inputText.val("012345678910");
        inputText.shouldHaveValue("0123456789");
    }

    @Test
    public void testInputNumberField() {
        InputText inputNumbers = page.widget(FormWidget.class).fields().field("Ввод чисел")
                .control(InputText.class);
        inputNumbers.shouldExists();
        inputNumbers.shouldBeEmpty();
        inputNumbers.val("0");
        inputNumbers.shouldHaveValue("0");
        inputNumbers.clickPlusStepButton();
        inputNumbers.shouldHaveValue("1");
        inputNumbers.clickPlusStepButton();
        inputNumbers.shouldHaveValue("2");
        inputNumbers.clickMinusStepButton();
        inputNumbers.shouldHaveValue("1");
        inputNumbers.val("835-#$7sd");
        inputNumbers.shouldHaveValue("8357");
    }

    @Test
    public void testDateInputField() {
        DateInput dateInput = page.widget(FormWidget.class).fields().field("Дата")
                .control(DateInput.class);
        dateInput.shouldExists();

        dateInput.shouldBeEmpty();
        dateInput.val("15.02.2020");
        dateInput.shouldHaveValue("15.02.2020");
        dateInput.clickCalendarButton();
        dateInput.shouldBeActiveDay("15");
        dateInput.clickDay("12");
        dateInput.shouldHaveValue("12.02.2020");
        // проверка месяцев и переход к предыдущему/следующему месяцу
        dateInput.clickCalendarButton();
        dateInput.shouldHaveCurrentMonth("Февраль");
        dateInput.shouldHaveCurrentYear("2020");
        dateInput.clickPreviousMonthButton();
        dateInput.shouldHaveCurrentMonth("Январь");
        dateInput.clickPreviousMonthButton();
        dateInput.shouldHaveCurrentMonth("Декабрь");
        dateInput.shouldHaveCurrentYear("2019");
        dateInput.clickNextMonthButton();
        dateInput.shouldHaveCurrentMonth("Январь");
        dateInput.val("15.02.2020");
        dateInput.shouldHaveValue("15.02.2020");
        dateInput.val("33.02.2020");
        dateInput.shouldHaveValue("15.02.2020");
        dateInput.val("15.24.2020");
        dateInput.shouldHaveValue("15.02.2020");
        dateInput.val("15.выап.2о2о");
        dateInput.shouldHaveValue("15.02.2020");
    }


    @Test
    public void testDateAndTimeInputField() {
        DateInput dateInput = page.widget(FormWidget.class).fields().field("Дата и время")
                .control(DateInput.class);
        dateInput.shouldExists();

        dateInput.shouldBeEmpty();
        dateInput.val("15.02.2020 00:00");
        dateInput.shouldHaveValue("15.02.2020 00:00");
        dateInput.clickCalendarButton();
        dateInput.timeVal("23", "59", "58");
        dateInput.shouldHaveValue("15.02.2020 23:59");
    }

    @Test
    public void testMaskedInputField() {
        MaskedInput maskedInput = page.widget(FormWidget.class).fields().field("Телефон")
                .control(MaskedInput.class);
        maskedInput.shouldExists();

        maskedInput.shouldHavePlaceholder("+7");
        maskedInput.shouldHaveValue("");
        maskedInput.val("A7$h-F835-#$7sd fr8!93+2~sr0");
        maskedInput.shouldHaveValue("+7 (783) 578-93-20");
    }


    @Test
    public void testInputMoneyField() {
        InputMoneyControl moneyInput = page.widget(FormWidget.class).fields().field("Ввод денег")
                .control(InputMoneyControl.class);
        moneyInput.shouldExists();

        moneyInput.shouldHaveValue("");
        moneyInput.shouldHavePlaceholder("");
        moneyInput.val("100500,999");
        moneyInput.shouldHaveValue("100 500,99 руб.");
    }

    //fieldSet2

    @Test
    public void testSelectInputField() {
        InputSelect selectInput = page.widget(FormWidget.class).fields().field("Выпадающий список")
                .control(InputSelect.class);
        selectInput.shouldExists();
        selectInput.shouldBeEmpty();
        selectInput.select(0);
        selectInput.shouldSelected("test1");
        selectInput.clear();
        selectInput.shouldBeEmpty();
        selectInput.select(2);
        selectInput.shouldSelected("test3");
        selectInput.clear();
        selectInput.shouldBeEmpty();
    }

    @Test
    public void testSelectCheckboxesField() {
        InputSelect selectInput = page.widget(FormWidget.class).fields().field("Множественный выбор")
                .control(InputSelect.class);
        selectInput.shouldExists();
        selectInput.shouldBeEmpty();
        selectInput.selectMulti(0);
        selectInput.shouldSelectedMulti("test1");
        selectInput.selectMulti(1, 2);
        selectInput.shouldSelectedMulti("test1", "test2", "test3");
        selectInput.clear();
        selectInput.shouldBeEmpty();
    }

    @Test
    public void testSliderField() {
        Slider slider = page.widget(FormWidget.class).fields().field("Слайдер")
                .control(Slider.class);
        slider.shouldExists();

        slider.shouldHaveValue("0");
        slider.val("10");
        slider.shouldHaveValue("10");
        slider.val("5");
        slider.shouldHaveValue("5");
        slider.val("0");
        slider.shouldHaveValue("0");
    }

    @Test
    public void testRadioGroupField() {
        RadioGroup radioGroup = page.widget(FormWidget.class).fields().field("Радио кнопки")
                .control(RadioGroup.class);
        radioGroup.shouldExists();

        radioGroup.shouldBeEmpty();
        radioGroup.check("Один");
        radioGroup.shouldBeChecked("Один");
        radioGroup.check("Три");
        radioGroup.shouldBeChecked("Три");
        radioGroup.check("Два");
        radioGroup.shouldBeChecked("Два");
    }

    @Test
    public void testCheckboxGroupField() {
        CheckboxGroup checkboxGroup = page.widget(FormWidget.class).fields().field("Группа чекбоксов")
                .control(CheckboxGroup.class);
        checkboxGroup.shouldExists();

        checkboxGroup.shouldBeEmpty();
        checkboxGroup.check("Один");
        checkboxGroup.shouldBeChecked("Один");
        checkboxGroup.check("Три");
        checkboxGroup.shouldBeChecked("Три");
        checkboxGroup.shouldBeUnchecked("Два");
        checkboxGroup.check("Два");
        checkboxGroup.shouldBeChecked("Два");
        checkboxGroup.uncheck("Один");
        checkboxGroup.shouldBeUnchecked("Один");
    }

}
