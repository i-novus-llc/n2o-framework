package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.*;
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
 * Класс проверки компонентов-полей на их очистку
 */
public class AllFieldsCleanableAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void allFieldsAreCleanable() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/all_cleanable/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        Checkbox checkbox = fields.field("checkbox").control(Checkbox.class);
        DateInput dateTime = fields.field("dateTime").control(DateInput.class);
        InputMoneyControl inputMoney = fields.field("inputMoney").control(InputMoneyControl.class);
        InputText inputText = fields.field("inputText").control(InputText.class);
        MaskedInput maskedInput = fields.field("maskedInput").control(MaskedInput.class);
        NumberPicker numberPicker = fields.field("numberPicker").control(NumberPicker.class);
        DateInterval dateInterval = fields.field("dateInterval").control(DateInterval.class);
        IntervalField interval = fields.field("range", IntervalField.class);
        CheckboxGroup checkboxGroup = fields.field("checkboxGroup").control(CheckboxGroup.class);
        InputSelect inputSelect = fields.field("inputSelect").control(InputSelect.class);
        Select select = fields.field("s1").control(Select.class);
        InputSelectTree inputSelectTree = fields.field("inputSelectTree").control(InputSelectTree.class);
        RadioGroup radioGroup = fields.field("radioGroup").control(RadioGroup.class);
        CodeEditor codeEditor = fields.field("codeEditor").control(CodeEditor.class);
        PasswordControl password = fields.field("password").control(PasswordControl.class);
        Slider slider = fields.field("slider").control(Slider.class);
        TextArea textarea = fields.field("textarea").control(TextArea.class);
        TextEditor textEditor = fields.field("textEditor").control(TextEditor.class);
        TimePicker timePicker = fields.field("timePicker").control(TimePicker.class);
        AutoComplete autoComplete = fields.field("autoComplete").control(AutoComplete.class);
        OutputList outputList = fields.field("outputList").control(OutputList.class);
        OutputText outputText = fields.field("outputText").control(OutputText.class);

        checkbox.shouldBeChecked();
        dateTime.shouldHaveValue("12.01.2024");
        inputMoney.shouldHaveValue("333");
        inputText.shouldHaveValue("value");
        maskedInput.shouldHaveValue("+7 (999) 999-99-99");
        numberPicker.shouldHaveValue("5");
        dateInterval.beginShouldHaveValue("10.01.2010");
        dateInterval.endShouldHaveValue("12.01.2024");
        interval.shouldExists();
        interval.begin(InputText.class).shouldHaveValue("3");
        interval.end(InputText.class).shouldHaveValue("5");
        checkboxGroup.check("test");
        checkboxGroup.shouldBeChecked("test");
        inputSelect.shouldHaveValue("1");
        select.shouldSelected("1");
        inputSelectTree.shouldHaveValue("1");
        radioGroup.check("test");
        radioGroup.shouldBeChecked("test");
        codeEditor.shouldHaveValue("value", 0);
        password.clickEyeButton();
        password.shouldHaveValue("value");
        slider.setValue("30");
        slider.shouldHaveValue("30");
        textarea.shouldHaveValue("value");
        textEditor.shouldHaveValue("value");
        timePicker.shouldHaveValue("02 ч 03 мин 05 сек");
        autoComplete.setValue("value");
        autoComplete.shouldHaveValue("value");
        outputList.shouldHaveValues(" ", new String[]{"outputList", "outputList", "outputList", "outputList", "outputList"});
        outputText.shouldHaveValue("value");

        page.widget(FormWidget.class).toolbar().topLeft().button("Clear").click();

        checkbox.shouldNotBeChecked();
        dateTime.shouldBeEmpty();
        inputMoney.shouldBeEmpty();
        inputText.shouldBeEmpty();
        maskedInput.shouldBeEmpty();
        numberPicker.shouldHaveValue("0");
        dateInterval.beginShouldBeEmpty();
        dateInterval.endShouldBeEmpty();
        interval.begin(InputText.class).shouldBeEmpty();
        interval.end(InputText.class).shouldBeEmpty();
        checkboxGroup.shouldBeUnchecked("test");
        inputSelect.shouldBeEmpty();
        select.shouldBeEmpty();
        inputSelectTree.shouldBeEmpty();
        radioGroup.shouldBeUnchecked("test");
        codeEditor.shouldBeEmpty();
        password.shouldBeEmpty();
        slider.shouldHaveValue("0");
        textarea.shouldBeEmpty();
        textEditor.shouldBeEmpty();
        timePicker.shouldBeEmpty();
        autoComplete.shouldBeEmpty();
        outputList.shouldBeEmpty();
        outputText.shouldBeEmpty();
    }
}

