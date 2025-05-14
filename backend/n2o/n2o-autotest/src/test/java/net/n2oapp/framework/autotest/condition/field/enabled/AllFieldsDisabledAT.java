package net.n2oapp.framework.autotest.condition.field.enabled;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
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
 * Автотест для проверки недоступности всех полей
 */
class AllFieldsDisabledAT extends AutoTestBase {

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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/enabled/all_field_disabled/index.page.xml")
        );
    }

    @Test
    void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        fields.field("button", ButtonField.class).shouldBeDisabled();
        fields.field("checkbox").control(Checkbox.class).shouldBeDisabled();
        fields.field("dateTime").control(DateInput.class).shouldBeDisabled();
        fields.field("fileUpload").control(FileUploadControl.class).shouldBeDisabled();
        fields.field("imageUpload").control(ImageUploadControl.class).shouldBeDisabled();
        fields.field("inputMoney").control(InputText.class).shouldBeDisabled();
        fields.field("inputText").control(InputText.class).shouldBeDisabled();
        fields.field("maskedInput").control(MaskedInput.class).shouldBeDisabled();
        fields.field("numberPicker").control(NumberPicker.class).shouldBeDisabled();

        fields.field("dateInterval").control(DateInterval.class).shouldBeDisabled();
        fields.field("intervalField", IntervalField.class).begin(InputText.class).shouldBeDisabled();
        fields.field("intervalField", IntervalField.class).end(InputText.class).shouldBeDisabled();
        fields.field("checkboxGroup").control(CheckboxGroup.class).shouldBeDisabled();
        fields.field("inputSelect").control(InputSelect.class).shouldBeDisabled();
        fields.field("select").control(Select.class).shouldBeDisabled();
        fields.field("inputSelectTree").control(InputSelectTree.class).shouldBeDisabled();
        fields.field("radioGroup").control(RadioGroup.class).shouldBeDisabled();
        fields.field("codeEditor").control(CodeEditor.class).shouldBeDisabled();

        fields.field("password").control(PasswordControl.class).shouldBeDisabled();
        fields.field("rating").control(Rating.class).shouldBeDisabled();
        fields.field("slider").control(Slider.class).shouldBeDisabled();
        fields.field("textarea").control(TextArea.class).shouldBeDisabled();
        fields.field("textEditor").control(TextEditor.class).shouldBeDisabled();
        fields.field("timePicker").control(TimePicker.class).shouldBeDisabled();
        fields.field("autoComplete").control(AutoComplete.class).shouldBeDisabled();
    }
}
