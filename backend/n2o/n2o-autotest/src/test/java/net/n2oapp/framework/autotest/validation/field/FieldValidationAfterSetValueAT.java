package net.n2oapp.framework.autotest.validation.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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
 * Автотест валидации для поля после set-value
 */
public class FieldValidationAfterSetValueAT extends AutoTestBase {

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
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/after_set_value/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/field/after_set_value/test.query.xml"));
    }

    @Test
    public void testValidation() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup radio = fields.field("type").control(RadioGroup.class);
        radio.shouldExists();
        StandardField nameField = fields.field("name");
        InputText name = nameField.control(InputText.class);
        name.shouldExists();

        radio.shouldBeChecked("Type1");
        name.shouldHaveValue("Test");
        name.shouldBeDisabled();

        radio.check("Type2");
        name.shouldBeEmpty();
        name.shouldBeEnabled();

        name.clear();
        nameField.shouldHaveValidationMessage(Condition.exist);

        radio.check("Type1");
        name.shouldHaveValue("Test");
        name.shouldBeDisabled();
        nameField.shouldHaveValidationMessage(Condition.empty);
    }
}
