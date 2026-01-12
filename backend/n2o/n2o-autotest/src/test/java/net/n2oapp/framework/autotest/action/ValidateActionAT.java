package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
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
 * Автотест для действия валидация на клиенте
 */
class ValidateActionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/test.object.xml")
        );
    }

    @Test
    void testValidateAction() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        StandardField nameField = form.fields().field("Имя");
        InputText name = nameField.control(InputText.class);
        StandardField birthdayField = form.fields().field("Дата рождения");
        MultiFieldSet multiFieldset = form.fieldsets().fieldset(1, MultiFieldSet.class);
        multiFieldset.clickAddButton();
        StandardField jobTitleField = multiFieldset.item(0).fields().field("Название");
        InputText jobTitle = jobTitleField.control(InputText.class);
        StandardField startYearField = multiFieldset.item(0).fields().field("Год начала");

        StandardButton checkBtn = form.toolbar().bottomLeft().button("Проверить");
        StandardButton saveBtn = form.toolbar().bottomLeft().button("Сохранить");
        checkBtn.click();

        nameField.shouldHaveValidationMessage(Condition.exist);
        nameField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        birthdayField.shouldHaveValidationMessage(Condition.empty);
        jobTitleField.shouldHaveValidationMessage(Condition.exist);
        jobTitleField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        startYearField.shouldHaveValidationMessage(Condition.empty);

        name.setValue("Иван");
        jobTitle.setValue("Инженер");

        checkBtn.click();

        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Имя и название заполнены верно");

        saveBtn.click();

        birthdayField.shouldHaveValidationMessage(Condition.exist);
        birthdayField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        startYearField.shouldHaveValidationMessage(Condition.exist);
        startYearField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
    }
}
