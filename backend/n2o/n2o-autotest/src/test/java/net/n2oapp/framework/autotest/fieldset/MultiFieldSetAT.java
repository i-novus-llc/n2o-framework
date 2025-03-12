package net.n2oapp.framework.autotest.fieldset;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
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
 * Автотест для филдсета с динамическим числом полей
 */
public class MultiFieldSetAT extends AutoTestBase {

    private SimplePage page;

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
    void testAdd() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/add/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        // 1.проверка, что при can-add="false" (нельзя добавить элемент)
        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.shouldExists();
        fieldset1.shouldNotHaveLabel();
        fieldset1.shouldBeEmpty();
        fieldset1.shouldNotHaveAddButton();

        // 2.стандартный случай
        MultiFieldSet fieldset2 = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset2.shouldExists();
        fieldset2.shouldHaveLabel("Заголовок");
        fieldset2.shouldHaveDescription("Подзаголовок филдсета");
        fieldset2.shouldHaveAddButton();
        fieldset2.addButtonShouldHaveLabel("Добавить участника");
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        fieldset2.shouldHaveItems(2);
        MultiFieldSetItem item1 = fieldset2.item(0);
        MultiFieldSetItem item2 = fieldset2.item(1);
        item1.shouldHaveLabel("Участник");
        item2.shouldHaveLabel("Участник №2");
        // проверяем корректность набора полей
        InputText name1 = item1.fields().field("name").control(InputText.class);
        name1.shouldExists();
        InputText age1 = item1.fields().field("age").control(InputText.class);
        age1.shouldExists();
        InputText name2 = item2.fields().field("name").control(InputText.class);
        name2.shouldExists();
        InputText age2 = item2.fields().field("age").control(InputText.class);
        age2.shouldExists();
        // задаем значения для полей первого элемента
        name1.click();
        name1.setValue("Joe");
        name1.shouldHaveValue("Joe");
        age1.click();
        age1.setValue("15");
        age1.shouldHaveValue("15");
        // проверяем, что значения не копируются в поля второго элемента
        name2.shouldBeEmpty();
        age2.shouldBeEmpty();
    }

    @Test
    void testRemove() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/remove/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        // 1.проверка при can-remove="false" (кнопок удаления нет)
        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.clickAddButton();
        fieldset1.clickAddButton();
        fieldset1.shouldNotHaveRemoveAllButton();
        MultiFieldSetItem item1 = fieldset1.item(0);
        MultiFieldSetItem item2 = fieldset1.item(1);
        item1.shouldNotHaveRemoveButton();
        item2.shouldNotHaveRemoveButton();

        // 2.стандартный случай
        MultiFieldSet fieldset2 = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        fieldset2.shouldHaveItems(3);
        item1 = fieldset2.item(0);
        item2 = fieldset2.item(1);
        MultiFieldSetItem item3 = fieldset2.item(2);
        InputText name1 = item1.fields().field("name").control(InputText.class);
        InputText name2 = item2.fields().field("name").control(InputText.class);
        InputText name3 = item3.fields().field("name").control(InputText.class);
        // проверяем наличие кнопок удалить у всех кроме первого элемента
        item1.shouldNotHaveRemoveButton();
        item2.shouldHaveRemoveButton();
        item3.shouldHaveRemoveButton();
        // задаем значения чтобы различать элементы
        name1.click();
        name1.setValue("AAA");
        name2.click();
        name2.setValue("BBB");
        name3.click();
        name3.setValue("CCC");
        // проверяем, что при удалении второго у третьего изменится подпись
        item2.clickRemoveButton();
        fieldset2.shouldHaveItems(2);
        item1.shouldHaveLabel("Участник 1");
        item2.shouldHaveLabel("Участник 2");
        name1.shouldHaveValue("AAA");
        name2.shouldHaveValue("CCC");

        // 3.проверка при can-remove-all="true" (удаление всех элементов кроме первого)
        MultiFieldSet fieldset3 = page.widget(FormWidget.class).fieldsets().fieldset(2, MultiFieldSet.class);
        fieldset3.clickAddButton();
        fieldset3.clickAddButton();
        fieldset3.clickAddButton();
        fieldset3.shouldHaveItems(3);
        // задаем значение только у первого элемента
        name1 = fieldset3.item(0).fields().field("name").control(InputText.class);
        name1.setValue("AAA");
        // проверяем кнопку удалить всех
        fieldset3.shouldHaveRemoveAllButton();
        fieldset3.removeAllButtonShouldHaveLabel("Удалить всех участников");
        fieldset3.clickRemoveAllButton();
        fieldset3.shouldHaveItems(1);
        name1.shouldHaveValue("AAA");

        // 4.проверка при can-remove-all="true" can-remove-first="true" (удаление всех элементов)
        MultiFieldSet fieldset4 = page.widget(FormWidget.class).fieldsets().fieldset(3, MultiFieldSet.class);
        fieldset4.clickAddButton();
        fieldset4.clickAddButton();
        fieldset4.clickAddButton();
        fieldset4.shouldHaveItems(3);
        fieldset4.clickRemoveAllButton();
        fieldset4.shouldBeEmpty();
    }

    @Test
    void testCopy() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/copy/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        // 1.стандартный случай (нет кнопки копирования)
        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.clickAddButton();
        fieldset1.clickAddButton();
        MultiFieldSetItem item1 = fieldset1.item(0);
        MultiFieldSetItem item2 = fieldset1.item(1);
        item1.shouldNotHaveCopyButton();
        item2.shouldNotHaveCopyButton();

        // 2.копирование включено
        MultiFieldSet fieldset2 = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        fieldset2.shouldHaveItems(2);
        item1 = fieldset2.item(0);
        item2 = fieldset2.item(1);
        item1.shouldHaveCopyButton();
        item2.shouldHaveCopyButton();
        // копируем второй элемент
        InputText name1 = item1.fields().field("name").control(InputText.class);
        InputText name2 = item2.fields().field("name").control(InputText.class);
        name2.click();
        name2.setValue("AAA");
        item2.clickCopyButton();
        fieldset2.shouldHaveItems(3);
        MultiFieldSetItem item3 = fieldset2.item(1);
        InputText name3 = item3.fields().field("name").control(InputText.class);
        name3.shouldHaveValue("AAA");
        // изменяем значение второго элемента и удаляем
        name2.click();
        name2.setValue("BBB");
        item2.clickRemoveButton();
        fieldset2.shouldHaveItems(2);
        // проверяем значение третьего элемента, который стал вторым
        name2.shouldHaveValue("AAA");
    }

    @Test
    void testNestedMultiFieldSet() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/nested/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.clickAddButton();
        MultiFieldSetItem item = fieldset1.item(0);
        item.fields().shouldHaveSize(1);
        MultiFieldSet fieldset2 = item.fieldsets().fieldset(MultiFieldSet.class);
        fieldset2.shouldExists();
        // проверяем функционал вложенного мультифилдсета
        // add
        fieldset2.shouldHaveAddButton();
        fieldset2.addButtonShouldHaveLabel("Добавить участника");
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        MultiFieldSetItem item1 = fieldset2.item(0);
        MultiFieldSetItem item2 = fieldset2.item(1);
        item1.shouldHaveLabel("Участник 1");
        item2.shouldHaveLabel("Участник 2");
        item1.shouldNotHaveRemoveButton();
        item1.shouldHaveCopyButton();
        item2.shouldHaveRemoveButton();
        item2.shouldHaveCopyButton();
        InputText name2 = item2.fields().field("name2").control(InputText.class);
        // copy
        name2.click();
        name2.setValue("AAA");
        item2.shouldHaveCopyButton();
        item2.clickCopyButton();
        fieldset2.shouldHaveItems(3);
        MultiFieldSetItem item3 = fieldset2.item(2);
        InputText name3 = item3.fields().field("name2").control(InputText.class);
        name3.shouldHaveValue("AAA");
        // remove
        fieldset2.clickAddButton();
        fieldset2.shouldHaveItems(4);
        item2.clickRemoveButton();
        fieldset2.shouldHaveItems(3);
        fieldset2.shouldHaveRemoveAllButton();
        fieldset2.removeAllButtonShouldHaveLabel("Удалить всех участников");
        fieldset2.clickRemoveAllButton();
        fieldset2.shouldHaveItems(1);
    }

    @Test
    void testQueryData() {
        setResourcePath("net/n2oapp/framework/autotest/fieldset/multiset/query_data");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/query_data/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/query_data/test.query.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        // проверяем наличие и значения полей
        fieldset.shouldHaveItems(2);
        MultiFieldSetItem item1 = fieldset.item(0);
        MultiFieldSetItem item2 = fieldset.item(1);
        item1.shouldHaveLabel("Участник 1");
        item2.shouldHaveLabel("Участник 2");
        InputText name1 = item1.fields().field("1.2.(1) name").control(InputText.class);
        name1.shouldExists();
        name1.shouldHaveValue("Joe");
        InputText age1 = item1.fields().field("1.2.(1) age").control(InputText.class);
        age1.shouldExists();
        age1.shouldHaveValue("15");
        InputText name2 = item2.fields().field("1.2.(2) name").control(InputText.class);
        name2.shouldExists();
        name2.shouldHaveValue("Ann");
        InputText age2 = item2.fields().field("1.2.(2) age").control(InputText.class);
        age2.shouldExists();
        age2.shouldBeEmpty();
        // проверяем, что при копировании ничего не теряется
        item1.clickCopyButton();
        fieldset.shouldHaveItems(3);
        MultiFieldSetItem item3 = fieldset.item(2);
        item3.shouldHaveLabel("Участник 3");
        InputText name3 = item3.fields().field("1.2.(3) name").control(InputText.class);
        name3.shouldHaveValue("Joe");
        InputText age3 = item3.fields().field("1.2.(3) age").control(InputText.class);
        age3.shouldHaveValue("15");
        age3.click();
        age3.setValue("30");
        // удаляем первый элемент и проверяем, что у третьего (теперь второго) не поменялись значения
        item1.clickRemoveButton();
        name2.shouldHaveValue("Joe");
        age2.shouldHaveValue("30");
    }

    @Test
    void testDependencies() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/dependencies/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.clickAddButton();
        fieldset1.clickAddButton();
        MultiFieldSetItem item1 = fieldset1.item(0);
        MultiFieldSetItem item2 = fieldset1.item(1);
        InputText name1 = item1.fields().field("name").control(InputText.class);
        InputText age1 = item1.fields().field("age").control(InputText.class);
        InputText name2 = item2.fields().field("name").control(InputText.class);
        InputText age2 = item2.fields().field("age").control(InputText.class);
        name1.shouldBeDisabled();
        name2.shouldBeDisabled();
        age1.click();
        age1.setValue("2");
        name1.shouldBeDisabled();
        name2.shouldBeDisabled();
        age2.click();
        age2.setValue("20");
        name1.shouldBeDisabled();
        name2.shouldBeEnabled();
        age1.click();
        age1.setValue("50");
        name1.shouldBeEnabled();
        name2.shouldBeEnabled();
        age2.click();
        age2.setValue("15");
        name1.shouldBeEnabled();
        name2.shouldBeDisabled();
    }

    @Test
    void testVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/visible/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(3);

        InputText inputText = fieldsets.fieldset(0, SimpleFieldSet.class).fields().field("test").control(InputText.class);
        inputText.shouldExists();

        MultiFieldSet multiSet1 = fieldsets.fieldset(1, MultiFieldSet.class);
        MultiFieldSet multiSet2 = fieldsets.fieldset(2, MultiFieldSet.class);
        multiSet1.shouldBeHidden();
        multiSet2.shouldBeHidden();

        inputText.click();
        inputText.setValue("test");
        multiSet1.shouldBeHidden();
        multiSet2.shouldBeVisible();
        multiSet2.shouldHaveAddButton();
    }

    @Test
    void testEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/enabled/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(3);

        InputText inputText = fieldsets.fieldset(0, SimpleFieldSet.class).fields().field("test").control(InputText.class);
        inputText.shouldExists();

        MultiFieldSet multiSet1 = fieldsets.fieldset(1, MultiFieldSet.class);
        MultiFieldSet multiSet2 = fieldsets.fieldset(2, MultiFieldSet.class);
        multiSet1.addButtonShouldBeDisabled();
        multiSet2.addButtonShouldBeDisabled();

        inputText.click();
        inputText.setValue("test");
        multiSet1.addButtonShouldBeDisabled();
        multiSet2.addButtonShouldBeEnabled();
    }

    @Test
    void testModalToModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/modal_to_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/modal_to_modal/update.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, N2oSimpleRegion.class).content().widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.shouldHaveAddButton();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem item = fieldset.item(0);
        item.shouldExists();
        OutputText id = item.fields().field("id").control(OutputText.class);
        id.shouldNotBeEmpty();

        InputText surnameMulti = item.fields().field("Фамилия").control(InputText.class);
        surnameMulti.click();
        surnameMulti.setValue("text");

        ButtonField changeBtnMulti = item.fields().field("Изменить", ButtonField.class);
        changeBtnMulti.click();

        StandardPage modalPage = N2oSelenide.modal().content(StandardPage.class);
        modalPage.shouldExists();

        FormWidget widgetModal = modalPage.regions().region(0, N2oSimpleRegion.class).content().widget(FormWidget.class);

        InputText surnameModal = widgetModal.fields().field("Фамилия").control(InputText.class);
        surnameModal.shouldHaveValue("text");
        surnameModal.click();
        surnameModal.setValue("text2");
        StandardButton changeBtnModal = widgetModal.toolbar().topLeft().button("Изменить");
        changeBtnModal.click();

        surnameMulti.shouldHaveValue("text2");

        item.shouldHaveCopyButton();
        item.clickCopyButton();

        MultiFieldSetItem itemCopy = fieldset.item(1);
        OutputText idCopy = itemCopy.fields().field("id").control(OutputText.class);
        idCopy.shouldNotHaveValue(id.getValue());

        ButtonField changeBtnMultiCopy = itemCopy.fields().field("Изменить", ButtonField.class);
        changeBtnMultiCopy.click();

        modalPage.shouldExists();

        surnameModal.click();
        surnameModal.setValue("textCopy");
        changeBtnModal.click();

        surnameMulti.shouldHaveValue("text2");
        itemCopy.fields().field("Фамилия").control(InputText.class).shouldHaveValue("textCopy");
    }

    @Test
    void testFiltering() {
        setResourcePath("net/n2oapp/framework/autotest/fieldset/multiset/filtering");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/filtering/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/filtering/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.shouldHaveAddButton();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();

        MultiFieldSetItem item = fieldset.item(0);
        item.shouldExists();
        InputText inputText = item.fields().field("chapter").control(InputText.class);
        InputSelect inputSelect = item.fields().field("Вид ТСР").control(InputSelect.class);

        inputText.click();
        inputText.setValue("1");
        inputSelect.click();
        inputSelect.dropdown().shouldExists();
        inputSelect.dropdown().shouldHaveOptions(1);
        inputSelect.dropdown().item(0).shouldHaveValue("test1");

        inputText.click();
        inputText.setValue("2");
        inputSelect.click();
        inputSelect.dropdown().shouldExists();
        inputSelect.dropdown().shouldHaveOptions(1);
        inputSelect.dropdown().item(0).shouldHaveValue("test2");
    }

    @Test
    void testValidation() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/validation/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();

        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(MultiFieldSet.class);
        fieldset.shouldHaveAddButton();
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

        surnameSecond.shouldHaveValidationMessage(Condition.empty);
        nameSecond.shouldHaveValidationMessage(Condition.empty);
        ageSecond.shouldHaveValidationMessage(Condition.empty);

        validateBtn.click();

        surnameSecond.control(InputText.class).click();
        surnameSecond.control(InputText.class).setValue("1");
        nameSecond.control(InputText.class).click();
        nameSecond.control(InputText.class).setValue("1");
        ageSecond.control(InputText.class).click();
        ageSecond.control(InputText.class).setValue("1");

        surnameSecond.shouldHaveValidationMessage(Condition.empty);
        nameSecond.shouldHaveValidationMessage(Condition.empty);
        ageSecond.shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    void testValidationOnClear() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/validation_when_clean/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();

        StandardField test = formWidget.fields().field("test");
        StandardField test2 = formWidget.fields().field("test");
        MultiFieldSet fieldset = formWidget.fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset.shouldHaveAddButton();
        fieldset.addButtonShouldBeEnabled();
        fieldset.clickAddButton();
        fieldset.clickAddButton();
        fieldset.clickAddButton();
        fieldset.clickAddButton();
        fieldset.item(0).fields().field("surname").control(InputText.class).setValue("test");
        fieldset.item(1).fields().field("name").control(InputText.class).setValue("test");
        fieldset.item(2).fields().field("age").control(InputText.class).setValue("3");
        fieldset.item(3).fields().field("name").control(InputText.class).setValue("test");
        formWidget.toolbar().topLeft().button("Validate").click();

        test.shouldHaveValidationMessage(Condition.exist);
        test2.shouldHaveValidationMessage(Condition.exist);

        fieldset.item(0).fields().field("name").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(0).fields().field("age").shouldHaveValidationMessage(Condition.exist);

        fieldset.item(1).fields().field("surname").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(1).fields().field("age").shouldHaveValidationMessage(Condition.exist);

        fieldset.item(2).fields().field("surname").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(2).fields().field("name").shouldHaveValidationMessage(Condition.exist);

        fieldset.item(3).fields().field("surname").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(3).fields().field("age").shouldHaveValidationMessage(Condition.exist);

        fieldset.item(1).clickRemoveButton();
        test.shouldHaveValidationMessage(Condition.exist);
        test2.shouldHaveValidationMessage(Condition.exist);

        fieldset.item(0).fields().field("surname").control(InputText.class).shouldHaveValue("test");
        fieldset.item(0).fields().field("name").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(0).fields().field("age").shouldHaveValidationMessage(Condition.exist);

        fieldset.item(1).fields().field("surname").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(1).fields().field("name").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(1).fields().field("age").control(InputText.class).shouldHaveValue("3");

        fieldset.item(2).fields().field("surname").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(2).fields().field("name").control(InputText.class).shouldHaveValue("test");
        fieldset.item(2).fields().field("age").shouldHaveValidationMessage(Condition.exist);

        fieldset.clickRemoveAllButton();
        test.shouldHaveValidationMessage(Condition.exist);
        test2.shouldHaveValidationMessage(Condition.exist);

        fieldset.shouldHaveItems(1);
        fieldset.item(0).fields().field("name").shouldHaveValidationMessage(Condition.exist);
        fieldset.item(0).fields().field("age").shouldHaveValidationMessage(Condition.exist);
    }

    @Test
    void testCreateMany() {
        setResourcePath("net/n2oapp/framework/autotest/fieldset/multiset/create_many");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/create_many/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/create_many/add.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/create_many/test.query.xml"));

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
        inputSelect.openPopup();
        inputSelect.dropdown().selectMulti(2, 3);
        modal.toolbar().bottomRight().button("Добавить").click();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset.shouldHaveItems(2);
        fieldset.item(0).fields().field("Имя").control(OutputText.class).shouldHaveValue("test3");
        fieldset.item(1).fields().field("Имя").control(OutputText.class).shouldHaveValue("test4");
        fieldset.item(1).fields().field("trash", ButtonField.class).click();
        fieldset.item(1).shouldNotExists();
    }

    @Test
    void testLabelResolve() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/label_resolve/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        InputText firstLabelDef = page.widget(FormWidget.class).fields().field("first label").control(InputText.class);
        InputText labelDef = page.widget(FormWidget.class).fields().field("default label").control(InputText.class);

        fieldset.clickAddButton();
        fieldset.item(0).shouldHaveLabel("test first");
        firstLabelDef.click();
        firstLabelDef.setValue("new first label");
        fieldset.item(0).shouldHaveLabel("new first label");
        fieldset.item(0).fields().field("Имя").control(InputText.class).click();
        fieldset.item(0).fields().field("Имя").control(InputText.class).setValue("Иван");
        fieldset.item(0).shouldHaveLabel("Иван");

        fieldset.clickAddButton();
        fieldset.item(1).shouldHaveLabel("test");
        labelDef.click();
        labelDef.setValue("new label");
        fieldset.item(1).shouldHaveLabel("new label");
        fieldset.item(1).fields().field("Имя").control(InputText.class).click();
        fieldset.item(1).fields().field("Имя").control(InputText.class).setValue("Петр");
        fieldset.item(1).shouldHaveLabel("Петр");
    }

    @Test
    void deleteItemWithSetValue() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/set_value/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        InputText input = page.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("input").control(InputText.class);

        fieldset.clickAddButton();

        MultiFieldSetItem item = fieldset.item(0);
        OutputText outputInFieldset = item.fields().field("output").control(OutputText.class);
        InputText inputInFieldset = item.fields().field("input").control(InputText.class);

        input.setValue("1234");
        outputInFieldset.shouldBeEmpty();
        inputInFieldset.setValue("43553");
        outputInFieldset.shouldNotBeEmpty();

        item.clickRemoveButton();

        fieldset.shouldHaveItems(0);
        input.shouldHaveValue("1234");
    }

    @Test
    void setRowIndexInFields() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/set_row_index/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(0, MultiFieldSet.class);

        fieldset.clickAddButton();

        Fields firstFields = fieldset.item(0).fields();
        OutputText value = firstFields.field("значение").control(OutputText.class);
        OutputText string = firstFields.field("строка").control(OutputText.class);
        OutputText doubleValue = firstFields.field("значение + значение").control(OutputText.class);
        OutputText valueAndStr = firstFields.field("значение + строка").control(OutputText.class);
        OutputText doubleStr = firstFields.field("строка + строка").control(OutputText.class);
        OutputText undefinedValue = firstFields.field("значение несуществующей переменной").control(OutputText.class);
        OutputText overrideValue = firstFields.field("переопределение переменной").control(OutputText.class);
        OutputText anotherString = firstFields.field("ещё строка").control(OutputText.class);

        value.shouldHaveValue("0");
        string.shouldHaveValue("index");
        doubleValue.shouldHaveValue("0");
        valueAndStr.shouldHaveValue("index0");
        doubleStr.shouldHaveValue("indexindex");
        undefinedValue.shouldHaveValue("undefined");
        overrideValue.shouldHaveValue("test");
        anotherString.shouldHaveValue("- index -");

        fieldset.clickAddButton();

        Fields secondFields = fieldset.item(1).fields();
        value = secondFields.field("значение").control(OutputText.class);
        string = secondFields.field("строка").control(OutputText.class);
        doubleValue = secondFields.field("значение + значение").control(OutputText.class);
        valueAndStr = secondFields.field("значение + строка").control(OutputText.class);
        doubleStr = secondFields.field("строка + строка").control(OutputText.class);
        undefinedValue = secondFields.field("значение несуществующей переменной").control(OutputText.class);
        overrideValue = secondFields.field("переопределение переменной").control(OutputText.class);
        anotherString = secondFields.field("ещё строка").control(OutputText.class);

        value.shouldHaveValue("1");
        string.shouldHaveValue("index");
        doubleValue.shouldHaveValue("2");
        valueAndStr.shouldHaveValue("index1");
        doubleStr.shouldHaveValue("indexindex");
        undefinedValue.shouldHaveValue("undefined");
        overrideValue.shouldHaveValue("test");
        anotherString.shouldHaveValue("- index -");
    }

    @Test
    void checkUpdateParentIndex() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/update_parent_index/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(0, MultiFieldSet.class);

        fieldset.clickAddButton();
        fieldset.clickAddButton();
        fieldset.clickAddButton();
        fieldset.clickAddButton();

        OutputText zero = fieldset.item(0).fields().field("0").control(OutputText.class);
        OutputText one = fieldset.item(1).fields().field("1").control(OutputText.class);
        OutputText two = fieldset.item(2).fields().field("2").control(OutputText.class);
        OutputText free = fieldset.item(3).fields().field("3").control(OutputText.class);

        zero.shouldHaveValue("0");
        one.shouldHaveValue("1");
        two.shouldHaveValue("2");
        free.shouldHaveValue("3");

        fieldset.item(2).clickRemoveButton();

        zero.shouldHaveValue("0");
        one.shouldHaveValue("1");
        two.shouldHaveValue("2");

        fieldset.item(0).clickRemoveButton();

        zero.shouldHaveValue("0");
        one.shouldHaveValue("1");
    }

    @Test
    void testResolveAttributes() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/resolve_attributes/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);

        Fields fields = form.fieldsets().fieldset(0, SimpleFieldSet.class).fields();
        Checkbox canAdd = fields.field("canAdd").control(Checkbox.class);
        Checkbox canCopy = fields.field("canCopy").control(Checkbox.class);
        Checkbox canRemove = fields.field("canRemove").control(Checkbox.class);
        Checkbox canRemoveFirst = fields.field("canRemoveFirst").control(Checkbox.class);
        Checkbox canRemoveAll = fields.field("canRemoveAll").control(Checkbox.class);
        InputText labelValue = fields.field("labelValue").control(InputText.class);

        MultiFieldSet fieldset = form.fieldsets().fieldset(1, MultiFieldSet.class);

        canAdd.shouldNotBeChecked();
        fieldset.shouldNotHaveAddButton();
        canAdd.setChecked(true);
        fieldset.shouldHaveAddButton();
        fieldset.clickAddButton();
        fieldset.clickAddButton();

        canCopy.shouldNotBeChecked();
        fieldset.item(0).shouldNotHaveCopyButton();
        fieldset.item(1).shouldNotHaveCopyButton();
        canCopy.setChecked(true);
        fieldset.item(0).shouldHaveCopyButton();
        fieldset.item(1).shouldHaveCopyButton();

        canRemove.shouldNotBeChecked();
        canRemoveFirst.shouldNotBeChecked();
        fieldset.item(0).shouldNotHaveRemoveButton();
        fieldset.item(1).shouldNotHaveRemoveButton();
        canRemove.setChecked(true);
        fieldset.item(0).shouldNotHaveRemoveButton();
        fieldset.item(1).shouldHaveRemoveButton();
        canRemoveFirst.setChecked(true);
        fieldset.item(0).shouldHaveRemoveButton();

        canRemoveAll.shouldNotBeChecked();
        fieldset.shouldNotHaveRemoveAllButton();
        canRemoveAll.setChecked(true);
        fieldset.shouldHaveRemoveAllButton();

        labelValue.shouldHaveValue("defaultValue");
        fieldset.item(0).shouldHaveLabel("defaultValue");
        fieldset.item(1).shouldHaveLabel("defaultValue");
        fieldset.addButtonShouldHaveLabel("defaultValue");
        fieldset.removeAllButtonShouldHaveLabel("defaultValue");
        labelValue.setValue("newValue");
        fieldset.item(0).shouldHaveLabel("newValue");
        fieldset.item(1).shouldHaveLabel("newValue");
        fieldset.addButtonShouldHaveLabel("newValue");
        fieldset.removeAllButtonShouldHaveLabel("newValue");
    }
}