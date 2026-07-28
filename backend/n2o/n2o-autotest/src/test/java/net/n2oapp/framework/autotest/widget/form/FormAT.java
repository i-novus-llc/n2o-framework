package net.n2oapp.framework.autotest.widget.form;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Html;
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
 * Автотест для виджета Форма
 */
class FormAT extends AutoTestBase {

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
    void testForm() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/simple/testForm.object.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(2);

        StandardField surname = form.fields().field("Фамилия");
        surname.shouldHaveLabelBy(Condition.text("Фамилия"));
        surname.control(InputText.class).click();
        surname.control(InputText.class).setValue("test");

        StandardField name = form.fields().field("Имя");
        name.control(InputText.class).click();
        surname.control(InputText.class).click();

        name.shouldBeRequired();
        name.shouldHaveValidationMessage(Condition.text(REQUIRED_VALIDATION_MESSAGE));

        name.control(InputText.class).click();
        name.control(InputText.class).setValue("1");
        surname.control(InputText.class).click();
        surname.control(InputText.class).setValue("test");
        name.shouldHaveValidationMessage(Condition.text("Имя должно быть test"));
        name.control(InputText.class).click();
        name.control(InputText.class).setValue("test");
        surname.control(InputText.class).click();
        surname.control(InputText.class).setValue("test");
        name.shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    void testToolbar() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/toolbar/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(1);
        InputText name = form.fields().field("Имя").control(InputText.class);

        // проверка, что при разном состоянии доступности кнопок отображаются разные подсказки
        StandardButton button1 = form.toolbar().bottomLeft().button("Кнопка1");
        StandardButton button2 = form.toolbar().bottomLeft().button("Кнопка2");

        // подсказка при недоступности кнопки1 и кнопки2
        button1.shouldBeDisabled();
        button1.hover();
        button1.tooltip().shouldHaveText(new String[]{"Заполните имя"});
        button2.shouldBeDisabled();
        button2.hover();
        button2.tooltip().shouldHaveText(new String[]{"Заполните имя"});

        name.click();
        name.setValue("test");
        // подсказка при доступности кнопки1 и кнопки2
        button1.shouldBeEnabled();
        button1.hover();
        button1.tooltip().shouldHaveText(new String[]{"Описание"});
        button2.shouldBeEnabled();
        button2.hover();
        // у кнопки2 не должно быть подсказки, т.к. не указан description
        button2.tooltip().shouldNotExists();
    }

    @Test
    void testFetchOnInitWithSetVisibility() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/fetch/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(2);
        form.fields().field(Html.class).shouldBeVisible();
        form.fields().field(Html.class).shouldHaveText("Html 1 при ините должно быть видно, после нажатия кнопки не видно");

        FieldSets fieldsets = form.fieldsets();
        SimpleFieldSet fieldset = fieldsets.fieldset(1, SimpleFieldSet.class);
        fieldset.shouldBeHidden();
        StandardField field = form.fields().field("Видно после нажатия кнопки");
        field.shouldBeHidden();

        form.fields().field("Кнопка", ButtonField.class).click();

        form.fields().field(Html.class).shouldBeVisible();
        form.fields().field(Html.class).shouldHaveText("Html 2 при ините не должно быть видно, после нажатия кнопки видно");

        fieldset.shouldBeVisible();
        fieldset.shouldHaveLabel("При ините не должно быть видно");
        field.shouldBeVisible();
    }
}
