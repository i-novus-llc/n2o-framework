package net.n2oapp.framework.autotest.validation.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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
 * Автотест валидации скрытых полей
 */
public class InvisibleFieldValidationAT extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/invisible/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/invisible/test.object.xml"));
    }

    @Test
    public void testValidation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup radio = fields.field("Родительское поле").control(RadioGroup.class);
        radio.shouldBeEmpty();
        radio.check("Показать поля");

        StandardField field = fields.field("Дочернее поле");
        StandardField fieldInSet = fields.field("Дочернее поле в невидимом филдсете");
        fieldInSet.shouldHaveValidationMessage(Condition.empty);
        StandardField fieldInSetInSet = fields.field("Дочернее поле в невидимом филдсете в филдсете");
        fieldInSetInSet.shouldHaveValidationMessage(Condition.empty);

        StandardButton validateBtn = page.widget(FormWidget.class).toolbar().topLeft().button("Валидировать");
        validateBtn.click();
        field.shouldHaveValidationMessage(Condition.exist);
        fieldInSet.shouldHaveValidationMessage(Condition.exist);
        fieldInSetInSet.shouldHaveValidationMessage(Condition.exist);


        radio.check("Скрыть поля");
        validateBtn.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
    }
}
