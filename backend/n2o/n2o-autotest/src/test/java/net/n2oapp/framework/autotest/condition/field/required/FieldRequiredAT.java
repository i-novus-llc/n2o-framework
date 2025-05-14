package net.n2oapp.framework.autotest.condition.field.required;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для проверки появления сообщения о обязательности поля
 */
class FieldRequiredAT extends AutoTestBase {
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
                new N2oAllPack()
        );
    }

    @Test
    void defaultValue() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/required/simpleValue/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/required/simpleValue/myObject.object.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();
        StandardField surNameField = formWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Фамилия (Заполнить только это поле!)");
        InputText inputTextSurname = surNameField.control(InputText.class);
        inputTextSurname.shouldExists();
        inputTextSurname.shouldBeEnabled();
        inputTextSurname.click();
        inputTextSurname.setValue("Фамилия");

        StandardButton button = formWidget.toolbar().bottomLeft().button("Создать");
        button.shouldBeEnabled();
        button.click();

        StandardField nameField = formWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Имя");
        nameField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        InputText inputTextName = nameField.control(InputText.class);
        inputTextName.shouldExists();
        inputTextName.shouldBeEnabled();
        inputTextName.click();
        inputTextName.setValue("Имя");

        button.click();

        nameField.shouldHaveValidationMessage(Condition.empty);

        page.alerts(Alert.PlacementEnum.top).alert(0).shouldHaveText("Данные сохранены");
    }
    
    @Test
    void expressionValue() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/required/expression/index.page.xml")
        );

        final SimplePage page = open(SimplePage.class);
        page.shouldExists();

        final Fields fields = page.widget(FormWidget.class).fields();
        final RadioGroup fieldEnabled = fields.field("type").control(RadioGroup.class);
        final InputText field = fields.field("message").control(InputText.class);

        fieldEnabled.check("required");
        
        field.click();
        fields.field("message").shouldBeRequired();

        fieldEnabled.check("not required");

        field.click();
        fields.field("message").shouldNotBeRequired();
    }
}
