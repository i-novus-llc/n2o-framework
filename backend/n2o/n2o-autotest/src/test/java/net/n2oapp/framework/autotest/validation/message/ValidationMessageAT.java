package net.n2oapp.framework.autotest.validation.message;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.control.TextArea;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
 * Автотест валидации обязательного поля
 */
public class ValidationMessageAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/message/test.object.xml"));
    }

    @Test
    public void testDependenciesValidation() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Dynamic validation tes");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);

        RadioGroup rg = form.fields().field("Requiring text").control(RadioGroup.class);
        rg.shouldBeChecked("requiring true");

        StandardField textAreaField = form.fields().field("Text-area");
        textAreaField.shouldBeRequired();
        textAreaField.shouldHaveValidationMessage(Condition.empty);
        textAreaField.control(TextArea.class).shouldBeEnabled();
        textAreaField.control(TextArea.class).shouldBeEmpty();

        form.toolbar().bottomLeft().button("Check").click();
        textAreaField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));

        rg.check("requiring false");
        textAreaField.shouldNotBeRequired();
        textAreaField.shouldHaveValidationMessage(Condition.empty);
        textAreaField.control(TextArea.class).shouldBeDisabled();

        rg.check("requiring true");
        textAreaField.shouldBeRequired();
        textAreaField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        textAreaField.control(TextArea.class).shouldBeEnabled();
    }

    @Test
    public void testConditionValidation() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Dynamic validation tes");

        FormWidget form = page.regions().region(1, SimpleRegion.class).content().widget(FormWidget.class);
        RadioGroup rg = form.fields().field("Make valid").control(RadioGroup.class);
        rg.shouldBeEmpty();

        StandardField field = form.fields().field("Field");
        field.shouldHaveValidationMessage(Condition.empty);

        InputText inputText = field.control(InputText.class);
        inputText.val("");

        StandardButton empty = form.toolbar().bottomLeft().button("Empty");
        empty.click();

        field.shouldHaveValidationMessage(Condition.text("Field is not valid"));

        rg.check("No");
        field.shouldHaveValidationMessage(Condition.empty);
        inputText.val("");
        empty.click();
        field.shouldHaveValidationMessage(Condition.empty);

        rg.check("Yes");
        field.shouldHaveValidationMessage(Condition.empty);
        inputText.val("");
        empty.click();
        field.shouldHaveValidationMessage(Condition.text("Field is not valid"));

        rg.check("No");
        field.shouldHaveValidationMessage(Condition.empty);
        inputText.val("");
        empty.click();
        field.shouldHaveValidationMessage(Condition.empty);
    }

}
