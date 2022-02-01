package net.n2oapp.framework.autotest.validation.message;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
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

/**
 * Автотест сообщений об ограничениях
 */
public class ConstraintValidationMessageAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/constraint/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/constraint/test.object.xml"));
    }

    @Test
    void testConstraintValidation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        FormWidget form = page.widget(FormWidget.class);

        StandardField firstName = fields.field("First name");
        InputText input1 = firstName.control(InputText.class);
        input1.val("test");

        StandardField middleName = fields.field("Middle name");
        InputText input2 = middleName.control(InputText.class);
        input2.val("test");

        StandardField lastName = fields.field("Last name");
        InputText input3 = lastName.control(InputText.class);
        input3.val("test");

        form.toolbar().bottomLeft().button("Create").click();
        firstName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));
        middleName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));

        input1.clear();
        firstName.shouldHaveValidationMessage(Condition.empty);
        middleName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));

        input1.val("name");
        form.toolbar().bottomLeft().button("Create").click();
        firstName.shouldHaveValidationMessage(Condition.empty);
        middleName.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));

        input2.clear();
        middleName.shouldHaveValidationMessage(Condition.empty);
        input2.val("name");
        form.toolbar().bottomLeft().button("Create").click();
        firstName.shouldHaveValidationMessage(Condition.empty);
        middleName.shouldHaveValidationMessage(Condition.empty);
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
    }
}
