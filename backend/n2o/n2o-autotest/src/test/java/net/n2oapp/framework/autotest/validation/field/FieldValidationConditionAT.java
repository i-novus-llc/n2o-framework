package net.n2oapp.framework.autotest.validation.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для условия выполнения валидации поля
 */
public class FieldValidationConditionAT extends AutoTestBase {

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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/validation/field/enabled/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/enabled/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testEnabled() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget widget = page.widget(FormWidget.class);
        StandardButton sendBtn = widget.toolbar().bottomLeft().button("Send");
        RadioGroup type = widget.fields().field("validation").control(RadioGroup.class);
        StandardField message = widget.fields().field("message");

        type.shouldBeEmpty();
        type.check("off");
        sendBtn.click();
        // ScriptEngine init waiting
        page.alerts().alert(0).element().waitUntil(Condition.exist, 8000);
        page.alerts().alert(0).shouldHaveText("Данные сохранены");

        message.control(InputText.class).shouldBeEmpty();
        type.check("on");
        sendBtn.click();
        message.shouldHaveValidationMessage(Condition.exist);
    }
}
