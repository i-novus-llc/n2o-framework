package net.n2oapp.framework.autotest.validation.constraint;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
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
 * Автотест сообщений об ограничениях
 */
public class ConstraintValidationAT extends AutoTestBase {

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

        setResourcePath("net/n2oapp/framework/autotest/validation/constraint");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/validation/constraint/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/constraint/test.object.xml"));
    }

    @Test
    void testConstraintValidation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        FormWidget form = page.widget(FormWidget.class);

        StandardField firstName = fields.field("First name");
        InputText input1 = firstName.control(InputText.class);
        input1.click();
        input1.setValue("test");

        StandardField middleName = fields.field("Middle name");
        InputText input2 = middleName.control(InputText.class);
        input2.click();
        input2.setValue("test");

        StandardField lastName = fields.field("Last name");
        InputText input3 = lastName.control(InputText.class);
        input3.click();
        input3.setValue("test");

        firstName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));
        middleName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));

        input1.click();
        input1.clear();
        firstName.shouldHaveValidationMessage(Condition.text("Имя уже существует"));
        middleName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));

        input1.click();
        input1.setValue("newName");
        firstName.shouldHaveValidationMessage(Condition.empty);
        middleName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));

        input2.click();
        input2.clear();
        middleName.shouldHaveValidationMessage(Condition.text("Имя уже существует"));
        input2.click();
        input2.setValue("newName");
        middleName.shouldHaveValidationMessage(Condition.empty);

        // проверка constraint валидации по мере ввода
        StandardField address = fields.field("Address");
        InputText input4 = address.control(InputText.class);
        input4.click();
        input4.setValue("addr");
        address.shouldHaveValidationMessage(Condition.empty);
        input4.setValue("address");
        address.shouldHaveValidationMessage(Condition.text("Адрес address уже существует"));
        firstName.shouldHaveValidationMessage(Condition.empty);
        middleName.shouldHaveValidationMessage(Condition.empty);
        form.toolbar().bottomLeft().button("Create").click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Данные сохранены");
        address.shouldHaveValidationMessage(Condition.text("Адрес address уже существует"));
        input4.setValue("address0");
        address.shouldHaveValidationMessage(Condition.empty);
    }
}
