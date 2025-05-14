package net.n2oapp.framework.autotest.validation.message;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
 * Автотест динамических сообщений валидации
 */
class DynamicValidationMessageAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testCondition() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/condition/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/condition/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Динамическое сообщение валидации");

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        StandardField field = form.fields().field("Имя");
        field.shouldExists();
        field.control(InputText.class).click();
        field.control(InputText.class).setValue("test");

        form.fields().field("Следующее поле").control(InputText.class).click();
        form.fields().field("Следующее поле").control(InputText.class).setValue("next");

        field.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));
    }

    @Test
    void testConstraint() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/constraint/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/constraint/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Динамическое сообщение валидации");

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        StandardField field1 = form.fields().field("Имя");
        field1.shouldExists();
        field1.control(InputText.class).click();
        field1.control(InputText.class).setValue("test");

        StandardField field2 = form.fields().field("Следующее поле");
        field2.control(InputText.class).click();
        field2.control(InputText.class).setValue("next");

        StandardButton button = form.toolbar().bottomLeft().button("Создать");
        button.shouldExists();
        button.click();

        field1.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));
    }
}
