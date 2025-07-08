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
class AllFieldsCleanableAT extends AutoTestBase {

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

        checkFields(fields);

        page.widget(FormWidget.class).toolbar().topLeft().button("Clear").click();

        checkFieldsAfterClear(fields);
    }

    private static void checkFields(Fields fields) {
        fields.field("checkbox").control(Checkbox.class).shouldBeChecked();
        fields.field("dateTime").control(DateInput.class).shouldHaveValue("12.01.2024");
        fields.field("inputMoney").control(InputMoneyControl.class).shouldHaveValue("333");
        fields.field("inputText").control(InputText.class).shouldHaveValue("value");
        fields.field("maskedInput").control(MaskedInput.class).shouldHaveValue("+7 (999) 999-99-99");
        fields.field("numberPicker").control(NumberPicker.class).shouldHaveValue("5");
        fields.field("dateInterval").control(DateInterval.class).beginShouldHaveValue("10.01.2010");
        fields.field("dateInterval").control(DateInterval.class).endShouldHaveValue("12.01.2024");
        fields.field("range", IntervalField.class).shouldExists();
        fields.field("range", IntervalField.class).begin(InputText.class).shouldHaveValue("3");
        fields.field("range", IntervalField.class).end(InputText.class).shouldHaveValue("5");
        fields.field("checkboxGroup").control(CheckboxGroup.class).check("test");
        fields.field("checkboxGroup").control(CheckboxGroup.class).shouldBeChecked("test");
        fields.field("inputSelect").control(InputSelect.class).shouldHaveValue("1");
        fields.field("s1").control(Select.class).shouldSelected("1");
        fields.field("inputSelectTree").control(InputSelectTree.class).shouldHaveValue("1");
        fields.field("radioGroup").control(RadioGroup.class).check("test");
        fields.field("radioGroup").control(RadioGroup.class).shouldBeChecked("test");
        fields.field("codeEditor").control(CodeEditor.class).shouldHaveValue("value", 0);
        fields.field("password").control(PasswordControl.class).clickEyeButton();
        fields.field("password").control(PasswordControl.class).shouldHaveValue("value");
        fields.field("slider").control(Slider.class).setValue("30");
        fields.field("slider").control(Slider.class).shouldHaveValue("30");
        fields.field("textarea").control(TextArea.class).shouldHaveValue("value");
        fields.field("textEditor").control(TextEditor.class).shouldHaveValue("value");
        fields.field("timePicker").control(TimePicker.class).shouldHaveValue("02 ч 03 мин 05 сек");
        fields.field("autoComplete").control(AutoComplete.class).setValue("value");
        fields.field("autoComplete").control(AutoComplete.class).shouldHaveValue("value");
        fields.field("outputList").control(OutputList.class).shouldHaveValues(" ", new String[]{"outputList", "outputList", "outputList", "outputList", "outputList"});
        fields.field("outputText").control(OutputText.class).shouldHaveValue("value");
    }

    private static void checkFieldsAfterClear(Fields fields) {
        fields.field("checkbox").control(Checkbox.class).shouldNotBeChecked();
        fields.field("dateTime").control(DateInput.class).shouldBeEmpty();
        fields.field("inputMoney").control(InputMoneyControl.class).shouldBeEmpty();
        fields.field("inputText").control(InputText.class).shouldBeEmpty();
        fields.field("maskedInput").control(MaskedInput.class).shouldBeEmpty();
        fields.field("numberPicker").control(NumberPicker.class).shouldHaveValue("0");
        fields.field("dateInterval").control(DateInterval.class).beginShouldBeEmpty();
        fields.field("dateInterval").control(DateInterval.class).endShouldBeEmpty();
        fields.field("range", IntervalField.class).begin(InputText.class).shouldBeEmpty();
        fields.field("range", IntervalField.class).end(InputText.class).shouldBeEmpty();
        fields.field("checkboxGroup").control(CheckboxGroup.class).shouldBeUnchecked("test");
        fields.field("inputSelect").control(InputSelect.class).shouldBeEmpty();
        fields.field("s1").control(Select.class).shouldBeEmpty();
        fields.field("inputSelectTree").control(InputSelectTree.class).shouldBeEmpty();
        fields.field("radioGroup").control(RadioGroup.class).shouldBeUnchecked("test");
        fields.field("codeEditor").control(CodeEditor.class).shouldBeEmpty();
        fields.field("password").control(PasswordControl.class).shouldBeEmpty();
        fields.field("slider").control(Slider.class).shouldHaveValue("0");
        fields.field("textarea").control(TextArea.class).shouldBeEmpty();
        fields.field("textEditor").control(TextEditor.class).shouldBeEmpty();
        fields.field("timePicker").control(TimePicker.class).shouldBeEmpty();
        fields.field("autoComplete").control(AutoComplete.class).shouldBeEmpty();
        fields.field("outputList").control(OutputList.class).shouldBeEmpty();
        fields.field("outputText").control(OutputText.class).shouldBeEmpty();
    }
}

