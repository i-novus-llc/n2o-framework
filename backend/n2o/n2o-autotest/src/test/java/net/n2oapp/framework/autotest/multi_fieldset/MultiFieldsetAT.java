package net.n2oapp.framework.autotest.multi_fieldset;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест по проверке работы компонента Мультифилдсет
 */

public class MultiFieldsetAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    public void testModalToModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/modal_to_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/modal_to_modal/update.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, N2oSimpleRegion.class).content().widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.addButtonShouldBeExist();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem item = fieldset.item(0);
        item.shouldExists();
        OutputText id = item.fields().field("id").control(OutputText.class);
        id.shouldNotBeEmpty();

        InputText surnameMulti = item.fields().field("Фамилия").control(InputText.class);
        surnameMulti.val("text");

        ButtonField changeBtnMulti = item.fields().field("Изменить", ButtonField.class);
        changeBtnMulti.click();

        StandardPage modalPage = N2oSelenide.modal().content(StandardPage.class);
        modalPage.shouldExists();

        FormWidget widgetModal = modalPage.regions().region(0, N2oSimpleRegion.class).content().widget(FormWidget.class);

        InputText surnameModal = widgetModal.fields().field("Фамилия").control(InputText.class);
        surnameModal.shouldHaveValue("text");
        surnameModal.val("text2");
        StandardButton changeBtnModal = widgetModal.toolbar().topLeft().button("Изменить");
        changeBtnModal.click();

        surnameMulti.shouldHaveValue("text2");

        item.copyButtonShouldExists();
        item.clickCopyButton();

        MultiFieldSetItem itemCopy = fieldset.item(1);
        OutputText idCopy = itemCopy.fields().field("id").control(OutputText.class);
        idCopy.shouldNotHaveValue(id.text());

        ButtonField changeBtnMultiCopy = itemCopy.fields().field("Изменить", ButtonField.class);
        changeBtnMultiCopy.click();

        modalPage.shouldExists();

        surnameModal.val("textCopy");
        changeBtnModal.click();

        surnameMulti.shouldHaveValue("text2");
        itemCopy.fields().field("Фамилия").control(InputText.class).shouldHaveValue("textCopy");
    }

    @Test
    public void testFiltering() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/filtering/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/filtering/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.addButtonShouldBeExist();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem item = fieldset.item(0);
        item.shouldExists();
        InputText inputText = item.fields().field("chapter").control(InputText.class);
        InputSelect inputSelect = item.fields().field("Вид ТСР").control(InputSelect.class);

        inputText.val("1");
        inputSelect.click();
        inputSelect.dropdown().shouldExists();
        inputSelect.dropdown().shouldHaveItems(1);
        //добавить проверку на изменение в фильтрации 1 - 1 2 -2
    }

    @Test
    public void testValidation() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/validation/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.addButtonShouldBeExist();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem itemFirst = fieldset.item(0);
        itemFirst.shouldExists();
        StandardField surnameFirst = itemFirst.fields().field("surname");
        StandardField nameFirst = itemFirst.fields().field("name");
        StandardField ageFirst = itemFirst.fields().field("age");

        StandardButton validateBtn = formWidget.toolbar().topLeft().button("Validate");
        validateBtn.click();

        surnameFirst.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        nameFirst.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        ageFirst.shouldHaveValidationMessage(Condition.text("Не заполнено поле"));

        fieldset.clickAddButton();
        itemFirst.clickRemoveButton();

        MultiFieldSetItem itemSecond = fieldset.item(0);
        StandardField surnameSecond = itemSecond.fields().field("surname");
        StandardField nameSecond = itemSecond.fields().field("name");
        StandardField ageSecond = itemSecond.fields().field("age");

        surnameSecond.shouldHaveValidationMessage(Condition.not(Condition.exist));
        nameSecond.shouldHaveValidationMessage(Condition.not(Condition.exist));
        ageSecond.shouldHaveValidationMessage(Condition.not(Condition.exist));

        validateBtn.click();

        surnameSecond.control(InputText.class).val("1");
        nameSecond.control(InputText.class).val("1");
        ageSecond.control(InputText.class).val("1");

        surnameSecond.shouldHaveValidationMessage(Condition.not(Condition.exist));
        nameSecond.shouldHaveValidationMessage(Condition.not(Condition.exist));
        ageSecond.shouldHaveValidationMessage(Condition.not(Condition.exist));
    }

    @Test
    public void testCreateMany() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/add.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        StandardButton addBtn = page.toolbar().bottomRight().button("Добавить");
        addBtn.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        StandardPage standardPage = modal.content(StandardPage.class);
        standardPage.shouldExists();

        FormWidget formWidget = standardPage.regions().region(0, N2oSimpleRegion.class).content().widget(FormWidget.class);
        InputSelect inputSelect = formWidget.fields().field("items").control(InputSelect.class);
        inputSelect.click();
        inputSelect.selectMulti(2, 3);
        modal.toolbar().bottomRight().button("Добавить").click();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset.shouldHaveItems(2);
        fieldset.item(0).fields().field("Имя").control(OutputText.class).shouldHaveValue("test3");
        fieldset.item(1).fields().field("Имя").control(OutputText.class).shouldHaveValue("test4");
        fieldset.item(1).fields().field("trash", ButtonField.class).click();
        fieldset.item(1).shouldNotExists();
    }
}
