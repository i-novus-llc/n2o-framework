package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
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

    public static final String SHOULD_HAVE_VALUE = "Следует заполнить поле";
    public static final String SUCCESS_MESSAGE = "Имя и название заполнены верно";

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
    }

    @Test
    void testValidateAction() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/simple/test.object.xml")
        );
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
        StandardButton saveBtn = form.toolbar().bottomLeft().button(SAVE_BUTTON_LABEL);
        checkBtn.click();

        nameField.shouldHaveValidationMessage(Condition.exist);
        nameField.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        birthdayField.shouldHaveValidationMessage(Condition.empty);
        jobTitleField.shouldHaveValidationMessage(Condition.exist);
        jobTitleField.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        startYearField.shouldHaveValidationMessage(Condition.empty);

        name.setValue("Иван");
        jobTitle.setValue("Инженер");

        checkBtn.click();

        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText(SUCCESS_MESSAGE);

        saveBtn.click();

        birthdayField.shouldHaveValidationMessage(Condition.exist);
        birthdayField.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        startYearField.shouldHaveValidationMessage(Condition.exist);
        startYearField.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));

        Selenide.refresh();
        name.setValue("test");
        page.widget(FormWidget.class).fields().field("Провалидировать часть полей", ButtonField.class).click();
        nameField.shouldHaveValidationMessage(Condition.empty);
        birthdayField.shouldHaveValidationMessage(Condition.empty);
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText(SUCCESS_ALERT_MESSAGE);
        page.alerts(Alert.PlacementEnum.TOP).alert(1).shouldHaveText(SUCCESS_MESSAGE);
    }

    @Test
    void testBreakOnDangerValidateElement() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/multi_action/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/multi_action/test.object.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);

        MultiFieldSet multiFieldset = form.fieldsets().fieldset(1, MultiFieldSet.class);
        multiFieldset.clickAddButton();

        StandardButton button = form.toolbar().bottomLeft().button("Провалидировать часть полей (danger)");
        button.click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldNotExists();

        form.fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        multiFieldset.item(0).fields().field("Название").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiFieldset.item(0).fields().field("Год начала").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год окончания").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));

        form.fields().field("Имя").control(InputText.class).setValue("test");
        multiFieldset.item(0).fields().field("Название").control(InputText.class).setValue("test");
        button.click();

        form.fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        multiFieldset.item(0).fields().field("Название").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год начала").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год окончания").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        alert.shouldHaveText(SUCCESS_ALERT_MESSAGE);
        alert.closeButton().click();

        form.fields().field("Дата рождения").control(DateInput.class).setValue("01.01.2000");
        multiFieldset.item(0).fields().field("Год окончания").control(DateInput.class).setValue("01.01.2026");
        button.click();
        alert.shouldHaveText(SUCCESS_ALERT_MESSAGE);
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.empty);
        form.fields().field("Год окончания").shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    void testBreakOnWarningValidateElement() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/multi_action/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/multi_action/test.object.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);

        MultiFieldSet multiFieldset = form.fieldsets().fieldset(1, MultiFieldSet.class);
        multiFieldset.clickAddButton();

        StandardButton button = form.toolbar().bottomLeft().button("Провалидировать часть полей (warning)");
        button.click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldNotExists();

        form.fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        multiFieldset.item(0).fields().field("Название").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiFieldset.item(0).fields().field("Год начала").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год окончания").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));

        form.fields().field("Имя").control(InputText.class).setValue("test");
        multiFieldset.item(0).fields().field("Название").control(InputText.class).setValue("test");
        button.click();
        alert.shouldNotExists();

        form.fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        multiFieldset.item(0).fields().field("Название").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год начала").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год окончания").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));

        multiFieldset.item(0).fields().field("Год окончания").control(DateInput.class).setValue("01.01.2026");
        form.fields().field("Дата рождения").control(DateInput.class).setValue("01.01.2000");
        button.click();
        alert.shouldHaveText(SUCCESS_ALERT_MESSAGE);
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.empty);
        form.fields().field("Год окончания").shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    void testBreakOnFalseValidateElement() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/multi_action/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/multi_action/test.object.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        MultiFieldSet multiFieldset = form.fieldsets().fieldset(1, MultiFieldSet.class);
        multiFieldset.clickAddButton();

        StandardButton button = form.toolbar().bottomLeft().button("Провалидировать часть полей (false)");
        button.click();

        form.fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        form.fields().field("Дата рождения").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        multiFieldset.item(0).fields().field("Название").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiFieldset.item(0).fields().field("Год начала").shouldHaveValidationMessage(Condition.empty);
        multiFieldset.item(0).fields().field("Год окончания").shouldHaveValidationMessage(Condition.text(SHOULD_HAVE_VALUE));
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText(SUCCESS_ALERT_MESSAGE);
        alert.closeButton().click();

        form.fields().field("Имя").control(InputText.class).setValue("test");
        form.fields().field("Дата рождения").control(DateInput.class).setValue("01.01.2000");
        multiFieldset.item(0).fields().field("Год начала").control(InputText.class).setValue("2025");
        multiFieldset.item(0).fields().field("Год окончания").control(DateInput.class).setValue("01.01.2026");
        multiFieldset.item(0).fields().field("Название").control(InputText.class).setValue("test");

        button.click();
        alert.shouldHaveText(SUCCESS_ALERT_MESSAGE);
    }

    @Test
    void testValidateNestedMultiSet() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/nested_multiset/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/validate/nested_multiset/test.object.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        // Валидация всех элементов внешнего списка
        validateAllOuterBtn(page);
        Selenide.refresh();
        // Валидация только первого элемента внешнего списка
        validateFirstOuterBtn(page);
        Selenide.refresh();
        // Валидация только первого элемента внутреннего списка
        validateFirstInnerBtn(page);
    }

    private void validateAllOuterBtn(SimplePage page) {
        FormWidget form = page.widget(FormWidget.class);
        MultiFieldSet multiSet = form.fieldsets().fieldset(0, MultiFieldSet.class);
        multiSet.clickAddButton("Добавить во внешний список");
        multiSet.clickAddButton("Добавить во внешний список");

        StandardField outerField0 = multiSet.item(0).fields().field("0. Внешнее поле");
        MultiFieldSet innerMultiSet0 = multiSet.item(0).fieldsets().fieldset(0, MultiFieldSet.class);
        innerMultiSet0.clickAddButton();
        innerMultiSet0.clickAddButton();
        StandardField innerField00 = innerMultiSet0.item(0).fields().field("0.0. Обязательное внутреннее поле");
        StandardField innerField01 = innerMultiSet0.item(1).fields().field("0.1. Обязательное внутреннее поле");

        StandardField outerField1 = multiSet.item(3).fields().field("1. Внешнее поле");
        MultiFieldSet innerMultiSet1 = multiSet.item(3).fieldsets().fieldset(0, MultiFieldSet.class);
        innerMultiSet1.clickAddButton();
        innerMultiSet1.clickAddButton();
        StandardField innerField10 = innerMultiSet1.item(0).fields().field("1.0. Обязательное внутреннее поле");
        StandardField innerField11 = innerMultiSet1.item(1).fields().field("1.1. Обязательное внутреннее поле");

        StandardButton validateAllOuterBtn = form.toolbar().topLeft().button("validate outerList.*");
        validateAllOuterBtn.click();

        outerField0.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField00.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField01.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        outerField1.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField10.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField11.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));

        outerField0.control(InputText.class).setValue("Тест");
        innerField00.control(InputText.class).setValue("Тест");
        innerField01.control(InputText.class).setValue("Тест");
        outerField1.control(InputText.class).setValue("Тест");
        innerField10.control(InputText.class).setValue("Тест");
        innerField11.control(InputText.class).setValue("Тест");
        validateAllOuterBtn.click();

        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText(SUCCESS_MESSAGE);
        outerField0.shouldHaveValidationMessage(Condition.empty);
        innerField00.shouldHaveValidationMessage(Condition.empty);
        innerField01.shouldHaveValidationMessage(Condition.empty);
        outerField1.shouldHaveValidationMessage(Condition.empty);
        innerField10.shouldHaveValidationMessage(Condition.empty);
        innerField11.shouldHaveValidationMessage(Condition.empty);
    }

    private void validateFirstOuterBtn(SimplePage page) {
        FormWidget form = page.widget(FormWidget.class);
        MultiFieldSet multiSet = form.fieldsets().fieldset(0, MultiFieldSet.class);
        multiSet.clickAddButton("Добавить во внешний список");
        multiSet.clickAddButton("Добавить во внешний список");

        StandardField outerField0 = multiSet.item(0).fields().field("0. Внешнее поле");
        MultiFieldSet innerMultiSet0 = multiSet.item(0).fieldsets().fieldset(0, MultiFieldSet.class);
        innerMultiSet0.clickAddButton();
        innerMultiSet0.clickAddButton();
        StandardField innerField00 = innerMultiSet0.item(0).fields().field("0.0. Обязательное внутреннее поле");
        StandardField innerField01 = innerMultiSet0.item(1).fields().field("0.1. Обязательное внутреннее поле");

        StandardField outerField1 = multiSet.item(3).fields().field("1. Внешнее поле");
        MultiFieldSet innerMultiSet1 = multiSet.item(3).fieldsets().fieldset(0, MultiFieldSet.class);
        innerMultiSet1.clickAddButton();
        innerMultiSet1.clickAddButton();
        StandardField innerField10 = innerMultiSet1.item(0).fields().field("1.0. Обязательное внутреннее поле");
        StandardField innerField11 = innerMultiSet1.item(1).fields().field("1.1. Обязательное внутреннее поле");

        StandardButton validateFirstOuterBtn = form.toolbar().topLeft().button("validate outerList[0].*");
        validateFirstOuterBtn.click();

        outerField0.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField00.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField01.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        outerField1.shouldHaveValidationMessage(Condition.empty);
        innerField10.shouldHaveValidationMessage(Condition.empty);
        innerField11.shouldHaveValidationMessage(Condition.empty);

        outerField0.control(InputText.class).setValue("Тест");
        innerField00.control(InputText.class).setValue("Тест");
        innerField01.control(InputText.class).setValue("Тест");
        validateFirstOuterBtn.click();

        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText(SUCCESS_MESSAGE);
        outerField0.shouldHaveValidationMessage(Condition.empty);
        innerField00.shouldHaveValidationMessage(Condition.empty);
        innerField01.shouldHaveValidationMessage(Condition.empty);
        outerField1.shouldHaveValidationMessage(Condition.empty);
        innerField10.shouldHaveValidationMessage(Condition.empty);
        innerField11.shouldHaveValidationMessage(Condition.empty);
    }

    private void validateFirstInnerBtn(SimplePage page) {
        FormWidget form = page.widget(FormWidget.class);

        MultiFieldSet multiSet = form.fieldsets().fieldset(0, MultiFieldSet.class);
        multiSet.clickAddButton("Добавить во внешний список");
        multiSet.clickAddButton("Добавить во внешний список");

        StandardField outerField0 = multiSet.item(0).fields().field("0. Внешнее поле");
        MultiFieldSet innerMultiSet0 = multiSet.item(0).fieldsets().fieldset(0, MultiFieldSet.class);
        innerMultiSet0.clickAddButton();
        innerMultiSet0.clickAddButton();
        StandardField innerField00 = innerMultiSet0.item(0).fields().field("0.0. Обязательное внутреннее поле");
        StandardField innerField01 = innerMultiSet0.item(1).fields().field("0.1. Обязательное внутреннее поле");

        StandardField outerField1 = multiSet.item(3).fields().field("1. Внешнее поле");
        MultiFieldSet innerMultiSet1 = multiSet.item(3).fieldsets().fieldset(0, MultiFieldSet.class);
        innerMultiSet1.clickAddButton();
        innerMultiSet1.clickAddButton();
        StandardField innerField10 = innerMultiSet1.item(0).fields().field("1.0. Обязательное внутреннее поле");
        StandardField innerField11 = innerMultiSet1.item(1).fields().field("1.1. Обязательное внутреннее поле");

        StandardButton validateFirstInnerBtn = form.toolbar().topLeft().button("validate outerList[0].innerList[0].*");
        validateFirstInnerBtn.click();

        outerField0.shouldHaveValidationMessage(Condition.empty);
        innerField00.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        innerField01.shouldHaveValidationMessage(Condition.empty);
        outerField1.shouldHaveValidationMessage(Condition.empty);
        innerField10.shouldHaveValidationMessage(Condition.empty);
        innerField11.shouldHaveValidationMessage(Condition.empty);

        innerField00.control(InputText.class).setValue("Тест");
        validateFirstInnerBtn.click();

        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText(SUCCESS_MESSAGE);
        outerField0.shouldHaveValidationMessage(Condition.empty);
        innerField00.shouldHaveValidationMessage(Condition.empty);
        innerField01.shouldHaveValidationMessage(Condition.empty);
        outerField1.shouldHaveValidationMessage(Condition.empty);
        innerField10.shouldHaveValidationMessage(Condition.empty);
        innerField11.shouldHaveValidationMessage(Condition.empty);
    }
}