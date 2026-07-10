package net.n2oapp.framework.autotest.widget.multiform;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.MultiFormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Автотест для виджета Мульти-форма
 */
class MultiFormAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testCreateUpdateDelete() {
        setResourcePath("net/n2oapp/framework/autotest/widget/multiform/");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/test.object.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        MultiFormWidget multiForm = page.widget(MultiFormWidget.class);
        multiForm.shouldHaveSize(2);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Иван");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Пётр");
        multiForm.paging().shouldHaveTotalElements(4);
        multiForm.paging().selectPage("2");
        multiForm.paging().shouldHaveActivePage("2");
        multiForm.shouldHaveSize(2);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Сидор");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Фёдор");

        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.shouldHaveSize(3);
        multiForm.paging().shouldHaveTotalElements(4);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldBeEmpty();
        InputText newName = multiForm.form(0).fields().field("Имя").control(InputText.class);
        newName.setValue("Семён");

        multiForm.form(0).fields().field("Сохранить", ButtonField.class).click();
        multiForm.shouldHaveSize(2);
        multiForm.paging().shouldHaveTotalElements(5);
        multiForm.paging().shouldHaveActivePage("2");
        multiForm.paging().selectPage("1");
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Семён");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Иван");


        InputText editName = multiForm.form(0).fields().field("Имя").control(InputText.class);
        editName.click();
        editName.setValue("Семён Изменённый");
        editName.shouldHaveValue("Семён Изменённый");
        multiForm.form(0).fields().field("Сохранить", ButtonField.class).click();
        multiForm.shouldHaveSize(2);
        multiForm.paging().shouldHaveTotalElements(5);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Семён Изменённый");

        multiForm.form(0).fields().field("Удалить", ButtonField.class).click();
        multiForm.paging().shouldHaveTotalElements(4);
        multiForm.shouldHaveSize(2);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Иван");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Пётр");
    }

    @Test
    void testUnsavedPromptDataTrue() {
        setResourcePath("net/n2oapp/framework/autotest/widget/multiform/");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/test.object.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        MultiFormWidget multiForm = page.widget(MultiFormWidget.class);
        multiForm.shouldHaveSize(2);
        multiForm.paging().shouldHaveActivePage("1");
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Иван");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Пётр");
        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.shouldHaveSize(3);
        multiForm.form(0).fields().field("Имя").control(InputText.class).setValue("Иван 2");
        multiForm.paging().selectPage("2");
        Alert alert = switchTo().alert();
        assertThat(alert.getText()).isEqualTo("Все несохраненные данные будут утеряны, вы уверены, что хотите уйти?");
        alert.dismiss();
        multiForm.paging().shouldHaveActivePage("1");
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Иван 2");

        multiForm.paging().selectPage("2");
        assertThat(alert.getText()).isEqualTo("Все несохраненные данные будут утеряны, вы уверены, что хотите уйти?");
        alert.accept();
        multiForm.paging().shouldHaveActivePage("2");
        multiForm.shouldHaveSize(2);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Сидор");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Фёдор");
    }


    @Test
    void testUnsavedPromptDataFalse() {
        setResourcePath("net/n2oapp/framework/autotest/widget/multiform/unsaved_data_prompt/");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/unsaved_data_prompt/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/unsaved_data_prompt/test.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        MultiFormWidget multiForm = page.widget(MultiFormWidget.class);
        multiForm.shouldHaveSize(2);
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Иван");
        multiForm.form(1).fields().field("Имя").control(InputText.class).shouldHaveValue("Пётр");

        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.shouldHaveSize(3);
        multiForm.form(0).fields().field("Имя").control(InputText.class).setValue("Иван 2");
        multiForm.paging().selectPage("2");
        multiForm.paging().shouldHaveActivePage("2");
        multiForm.form(0).fields().field("Имя").control(InputText.class).shouldHaveValue("Сидор");
    }

    @Test
    void testValidations() {
        setResourcePath("net/n2oapp/framework/autotest/widget/multiform/validations/");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/validations/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/validations/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/multiform/validations/test.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        MultiFormWidget multiForm = page.widget(MultiFormWidget.class);
        multiForm.shouldHaveSize(2);
        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.form(0).fields().field("Имя").control(InputText.class).click();
        multiForm.form(0).fields().field("Фамилия").control(InputText.class).setValue("Пе");
        multiForm.form(0).fields().field("Имя").control(InputText.class).click();
        multiForm.form(0).fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiForm.form(0).fields().field("Фамилия").shouldHaveValidationMessage(Condition.text("Сработала валидация"));
        multiForm.form(1).fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        multiForm.form(1).fields().field("Фамилия").shouldHaveValidationMessage(Condition.empty);

        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.form(0).fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        multiForm.form(0).fields().field("Фамилия").shouldHaveValidationMessage(Condition.empty);
        multiForm.form(1).fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiForm.form(1).fields().field("Фамилия").shouldHaveValidationMessage(Condition.text("Сработала валидация"));

        multiForm.form(0).fields().field("Удалить", ButtonField.class).click();
        multiForm.form(0).fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiForm.form(0).fields().field("Фамилия").shouldHaveValidationMessage(Condition.text("Сработала валидация"));
        multiForm.form(1).fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        multiForm.form(1).fields().field("Фамилия").shouldHaveValidationMessage(Condition.empty);

        multiForm.form(0).fields().field("Удалить", ButtonField.class).click();
        multiForm.form(1).fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        multiForm.form(1).fields().field("Фамилия").shouldHaveValidationMessage(Condition.empty);

        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.form(0).fields().field("Имя").control(InputText.class).click();
        multiForm.form(0).fields().field("Фамилия").control(InputText.class).setValue("Пе");
        multiForm.form(0).fields().field("Имя").control(InputText.class).click();
        multiForm.form(0).fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));
        multiForm.form(0).fields().field("Фамилия").shouldHaveValidationMessage(Condition.text("Сработала валидация"));
        multiForm.paging().selectPage("2");
        multiForm.paging().shouldHaveActivePage("2");
        multiForm.form(0).fields().field("Имя").shouldHaveValidationMessage(Condition.empty);
        multiForm.form(0).fields().field("Фамилия").shouldHaveValidationMessage(Condition.empty);

        multiForm.toolbar().topLeft().button("Добавить").click();
        multiForm.form(0).fields().field("update", ButtonField.class).click();
        multiForm.form(0).fields().field("Имя").shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));

    }
}
