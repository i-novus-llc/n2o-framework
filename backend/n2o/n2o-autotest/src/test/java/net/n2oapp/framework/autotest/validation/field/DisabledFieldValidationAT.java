package net.n2oapp.framework.autotest.validation.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
 * Автотест валидации скрытых полей
 */
public class DisabledFieldValidationAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/disabled/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/disabled/test.object.xml"));
    }

    @Test
    void testValidation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        StandardButton validateBtn = page.widget(FormWidget.class).toolbar().topLeft().button("Валидировать");
        Fields fields = page.widget(FormWidget.class).fields();
        InputText name = fields.field("Имя").control(InputText.class);
        StandardField age = fields.field("Возраст");

        name.setValue("Den");
        validateBtn.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Данные сохранены");

        name.setValue("Anna");
        validateBtn.click();
        age.shouldHaveValidationMessage(Condition.exist);
    }
}
