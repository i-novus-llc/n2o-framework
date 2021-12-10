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
public class DynamicValidationMessageAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testCondition() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/condition/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/condition/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Динамическое сообщение валидации");

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        StandardField field = form.fields().field("Имя");
        field.shouldExists();
        field.control(InputText.class).val("test");

        form.fields().field("Следующее поле").control(InputText.class).val("next");

        field.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));
    }

    @Test
    public void testConstraint() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/constraint/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/dynamic/constraint/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Динамическое сообщение валидации");

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        StandardField field = form.fields().field("Имя");
        field.shouldExists();
        StandardButton button = form.toolbar().bottomLeft().button("Создать");
        button.shouldExists();

        field.control(InputText.class).val("test");
        button.click();

        field.shouldHaveValidationMessage(Condition.text("Имя test уже существует"));
    }
}
