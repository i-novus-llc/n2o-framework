package net.n2oapp.framework.autotest.validation.disable;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
 * Автотест на срабатывание валидации полей
 */
class DisableValidationAT extends AutoTestBase {
    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/validation/disable/index.page.xml"));
    }

    @Test
    void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Toolbar toolbar = page.widget(FormWidget.class).toolbar().bottomLeft();
        StandardButton validateBtn = toolbar.button("Валидировать");
        Fields fields = page.widget(FormWidget.class).fields();

        // field-dependency

        /// requiring
        InputText input = fields.field("input").control(InputText.class);
        StandardField field1 = fields.field("requiring");
        StandardField field2 = fields.field("requiringValidate");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.empty);
        input.setValue("1");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.text("Значение не должно быть пустым"));
        field1.control(InputText.class).setValue("любой текст");
        field2.control(InputText.class).setValue("любой текст");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.empty);
        validateBtn.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Готово");

        /// set-value
        InputText value = fields.field("value").control(InputText.class);
        field1 = fields.field("setValue");
        field2 = fields.field("setValueValidate");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.empty);
        value.setValue("1");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.text("Значение не должно быть 1"));
        validateBtn.click();
        field1.shouldHaveValidationMessage(Condition.text("Значение не должно быть 1"));
        field2.shouldHaveValidationMessage(Condition.text("Значение не должно быть 1"));
        value.setValue("2");
        field1.shouldHaveValidationMessage(Condition.text("Значение не должно быть 1"));
        field2.shouldHaveValidationMessage(Condition.empty);
        validateBtn.click();
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.empty);

        checkReset(fields);

        checkTargetField(toolbar, validateBtn, fields.field("target"));
    }

    private static void checkReset(Fields fields) {
        /// reset
        Checkbox checkbox = fields.field("checkbox").control(Checkbox.class);
        checkbox.shouldNotBeChecked();
        StandardField field1 = fields.field("reset");
        StandardField field2 = fields.field("resetValidate");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.empty);
        checkbox.setChecked(true);
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.text("Значение не должно быть пустым"));
        field1.control(InputText.class).setValue("текст");
        field2.control(InputText.class).setValue("текст");
        field1.shouldHaveValidationMessage(Condition.empty);
        field2.shouldHaveValidationMessage(Condition.empty);
    }

    private static void checkTargetField(Toolbar toolbar, StandardButton validateBtn, StandardField targetField) {
        // set-value action
        targetField.control(InputText.class).shouldBeEmpty();
        toolbar.button("set target = 2").click();
        targetField.control(InputText.class).shouldHaveValue("2");
        targetField.shouldHaveValidationMessage(Condition.empty);
        validateBtn.click();
        targetField.shouldHaveValidationMessage(Condition.text("Значение должно быть пустым"));
        targetField.control(InputText.class).clear();
        validateBtn.click();
        toolbar.button("set target = 2 and validate").click();
        targetField.control(InputText.class).shouldHaveValue("2");
        targetField.shouldHaveValidationMessage(Condition.text("Значение должно быть пустым"));
        targetField.control(InputText.class).clear();
        validateBtn.click();
        targetField.shouldHaveValidationMessage(Condition.empty);

        // copy action
        toolbar.button("copy source to target").click();
        targetField.control(InputText.class).shouldHaveValue("1");
        targetField.shouldHaveValidationMessage(Condition.empty);
        validateBtn.click();
        targetField.shouldHaveValidationMessage(Condition.text("Значение должно быть пустым"));
        targetField.control(InputText.class).clear();
        validateBtn.click();
        toolbar.button("copy source to target and validate").click();
        targetField.control(InputText.class).shouldHaveValue("1");
        targetField.shouldHaveValidationMessage(Condition.text("Значение должно быть пустым"));
        targetField.control(InputText.class).clear();
        validateBtn.click();
        targetField.shouldHaveValidationMessage(Condition.empty);
    }
}
