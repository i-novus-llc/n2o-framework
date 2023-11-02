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
    void test() {
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
}
