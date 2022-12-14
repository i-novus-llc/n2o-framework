package net.n2oapp.framework.autotest.multi_fieldset;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.modal.N2oModal;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testModalToModal() throws InterruptedException {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/modal_to_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/modal_to_modal/update.page.xml"));
        Configuration.headless=false;

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

        item.removeButtonShouldExists();
        item.clickRemoveButton();
        item.shouldNotExists();
        itemCopy.shouldExists();
        fieldset.removeAllButtonShouldBeExist();
        fieldset.clickRemoveAllButton();
        itemCopy.shouldNotExists();
    }

    /**
     * 1. Клик по кнопке "Добавить период"
     * - Проверить наличие id
     * 2. Ввести что-то в поля
     * 3. Кликнуть "Изменить"
     * - поля в модалке сохранили значение
     * 4. кликнуть "Изменить
     * - значение не поменялось
     * 5. Добавить еще строку
     * 5. Кликкнуть снова "изменить"
     * 6. ввести новое значение
     * 7. поменять старое значение
     * - Проверить, что значения поменялись
     * 8. Скопировать строку
     * - проверить что id тот же / либо другой
     * - значения те же
     * 9. Изменить что-то в скопированной строке
     * - поменялось в ней
     * - не поменялось в исходной
     * 10. Удалить Исходную
     * - првоерить что удалилась
     * 11. Удалить все
     * - проверить что все удалилось
     */

    @Test
    public void testFiltering() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/filtering/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/filtering/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.addButtonShouldBeExist();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem item = fieldset.item(0);
        InputText inputText = item.fields().field("chapter").control(InputText.class);
        InputSelect inputSelect = item.fields().field("Вид ТСР").control(InputSelect.class);

        inputText.val("1");
        inputSelect.click();
        inputSelect.dropdown().shouldHaveItems(1);
    }

    /**
     * 1. Добавить строку
     * 2. Ввести 1 или 2
     * - првоерить в что выпадающем списке соответствующее значение
     * @throws InterruptedException
     */

    @Test
    public void testValidation() throws InterruptedException {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/validation/index.page.xml"));
//        Configuration.headless=false;

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.addButtonShouldBeExist();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem itemFirst = fieldset.item(0);
        InputText surnameFirst = itemFirst.fields().field("surname").control(InputText.class);
        InputText nameFirst = itemFirst.fields().field("name").control(InputText.class);
        InputText ageFirst = itemFirst.fields().field("age").control(InputText.class);

        StandardButton validateBtn = formWidget.toolbar().topLeft().button("Validate");
        validateBtn.click();

        surnameFirst.shouldHaveValidationMessage("Поле обязательно для заполнения");
        surnameFirst.validationMessageShouldBe("text-danger");
        nameFirst.shouldHaveValidationMessage("Поле обязательно для заполнения");
        nameFirst.validationMessageShouldBe("text-danger");
        ageFirst.shouldHaveValidationMessage("Не заполнено поле");
        ageFirst.validationMessageShouldBe("text-danger");

        fieldset.clickAddButton();

        itemFirst.clickRemoveButton();

        MultiFieldSetItem itemSecond = fieldset.item(0);
        InputText surnameSecond = itemSecond.fields().field("surname").control(InputText.class);
        InputText nameSecond = itemSecond.fields().field("name").control(InputText.class);
        InputText ageSecond = itemSecond.fields().field("age").control(InputText.class);

        surnameSecond.shouldHaveNotValidationMessage();
        nameSecond.shouldHaveNotValidationMessage();
        ageSecond.shouldHaveNotValidationMessage();

        validateBtn.click();

        surnameSecond.val("1");
        nameSecond.val("1");
        ageSecond.val("1");

        surnameSecond.shouldHaveNotValidationMessage();
        nameSecond.shouldHaveNotValidationMessage();
        ageSecond.shouldHaveNotValidationMessage();
    }

    /**
     * 1. Добавить строку
     * 2. Кликнуть на поле
     * - проверить что загорелось красным
     * 3. Ввести в тоже поле значение
     * - проверить что валидация снялась
     * 4. Добавить две новых строки
     * 5. Кликнуть в каждое
     * 6. Добавить две строки и ввести значения в те поля, где сработала валидация
     * 7. Удалить две старые
     * - проверить что валидация не перешла на новые поля со значениями
     */

    @Test
    public void testCreateMany() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/add.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/update.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/create_many/test.query.xml"));
        Configuration.headless=false;

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        StandardButton addBtn = page.toolbar().bottomRight().button("Добавить");
        addBtn.click();


    }

    /**
     * 1. Нажать добавить
     * 2. Выбрать итем
     * 3. Добавить
     * - првоерить что появился
     * 4. кликнуть на мусорку
     * - проверить что больше его нет
     */
}
