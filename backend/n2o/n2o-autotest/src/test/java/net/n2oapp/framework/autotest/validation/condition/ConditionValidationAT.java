package net.n2oapp.framework.autotest.validation.condition;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
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

public class ConditionValidationAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testCondition() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/validation/condition/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget widget = page.widget(FormWidget.class);
        InputText conditionInput = widget.fields().field("conditionInput").control(InputText.class);
        Checkbox checkbox1 = widget.fields().field("1").control(Checkbox.class);
        Checkbox checkbox2 = widget.fields().field("2").control(Checkbox.class);
        StandardField testInput = widget.fields().field("testInput");
        Button validateButton = widget.toolbar().bottomLeft().button("validate");

        testInput.shouldHaveValidationMessage(Condition.empty);
        validateButton.click();
        testInput.shouldHaveValidationMessage(Condition.empty);
        conditionInput.setValue("te");
        validateButton.click();
        testInput.shouldHaveValidationMessage(Condition.empty);
        conditionInput.setValue("test");
        validateButton.click();
        testInput.shouldHaveValidationMessage(Condition.exist);
        testInput.shouldHaveValidationMessage(Condition.text("Обязательно если test"));
        testInput.control(InputText.class).setValue("value");
        testInput.shouldHaveValidationMessage(Condition.empty);

        testInput.control(InputText.class).clear();
        conditionInput.clear();
        validateButton.click();
        testInput.shouldHaveValidationMessage(Condition.empty);

        checkbox1.setChecked(true);
        checkbox2.setChecked(true);
        validateButton.click();
        testInput.shouldHaveValidationMessage(Condition.exist);
        testInput.shouldHaveValidationMessage(Condition.text("Обязательно если выбран чекбокс 1"));

        checkbox1.setChecked(false);
        validateButton.click();
        testInput.shouldHaveValidationMessage(Condition.exist);
        testInput.shouldHaveValidationMessage(Condition.text("Обязательно если выбран чекбокс 2"));
        testInput.control(InputText.class).setValue("value");
        testInput.shouldHaveValidationMessage(Condition.empty);

        conditionInput.setValue("test");
        testInput.control(InputText.class).clear();
        testInput.shouldHaveValidationMessage(Condition.exist);
        testInput.shouldHaveValidationMessage(Condition.text("Обязательно если test"));
    }

    /** on-валидации, если на поле больше одной валидации **/
    @Test
    void testTwoValidations() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/validation/two_validations/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget widget = page.widget(FormWidget.class);
        Button validateButton = widget.toolbar().topLeft().button("validate");
        Checkbox checkbox = widget.fields().field("valid").control(Checkbox.class);
        StandardField requiredInput = widget.fields().field("required");
        StandardField partialInput = widget.fields().field("partial");

        validateButton.click();
        requiredInput.shouldExists();
        requiredInput.shouldHaveValidationMessage(Condition.exist);
        requiredInput.shouldHaveValidationMessage(Condition.text("condition1 is invalid"));

        partialInput.shouldExists();
        partialInput.shouldHaveValidationMessage(Condition.exist);
        partialInput.shouldHaveValidationMessage(Condition.text("condition2 is invalid"));

        checkbox.setChecked(true);
        requiredInput.shouldHaveValidationMessage(Condition.empty);
        partialInput.shouldHaveValidationMessage(Condition.empty);

        checkbox.setChecked(false);
        requiredInput.shouldHaveValidationMessage(Condition.exist);
        partialInput.shouldHaveValidationMessage(Condition.exist);
    }
}