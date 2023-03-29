package net.n2oapp.framework.autotest.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
    public void testAdd() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/add/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        // 1.проверка, что при can-add="false" (нельзя добавить элемент)
        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.shouldExists();
        fieldset1.shouldNotHaveLabel();
        fieldset1.shouldBeEmpty();
        fieldset1.addButtonShouldNotBeExist();

        // 2.стандартный случай
        MultiFieldSet fieldset2 = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset2.shouldExists();
        fieldset2.shouldHaveLabel("Заголовок");
        fieldset2.shouldHaveDescription("Подзаголовок филдсета");
        fieldset2.addButtonShouldBeExist();
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
        name1.val("Joe");
        name1.shouldHaveValue("Joe");
        age1.val("15");
        age1.shouldHaveValue("15");
        // проверяем, что значения не копируются в поля второго элемента
        name2.shouldBeEmpty();
        age2.shouldBeEmpty();
    }

    @Test
    public void testRemove() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/remove/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        // 1.проверка при can-remove="false" (кнопок удаления нет)
        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.clickAddButton();
        fieldset1.clickAddButton();
        fieldset1.removeAllButtonShouldNotBeExist();
        MultiFieldSetItem item1 = fieldset1.item(0);
        MultiFieldSetItem item2 = fieldset1.item(1);
        item1.removeButtonShouldNotExists();
        item2.removeButtonShouldNotExists();

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
        item1.removeButtonShouldNotExists();
        item2.removeButtonShouldExists();
        item3.removeButtonShouldExists();
        // задаем значения чтобы различать элементы
        name1.val("AAA");
        name2.val("BBB");
        name3.val("CCC");
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
        name1.val("AAA");
        // проверяем кнопку удалить всех
        fieldset3.removeAllButtonShouldBeExist();
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
    public void testCopy() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/copy/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        // 1.стандартный случай (нет кнопки копирования)
        MultiFieldSet fieldset1 = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        fieldset1.clickAddButton();
        fieldset1.clickAddButton();
        MultiFieldSetItem item1 = fieldset1.item(0);
        MultiFieldSetItem item2 = fieldset1.item(1);
        item1.copyButtonShouldNotExists();
        item2.copyButtonShouldNotExists();

        // 2.копирование включено
        MultiFieldSet fieldset2 = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        fieldset2.shouldHaveItems(2);
        item1 = fieldset2.item(0);
        item2 = fieldset2.item(1);
        item1.copyButtonShouldExists();
        item2.copyButtonShouldExists();
        // копируем второй элемент
        InputText name2 = item2.fields().field("name").control(InputText.class);
        name2.val("AAA");
        item2.clickCopyButton();
        fieldset2.shouldHaveItems(3);
        MultiFieldSetItem item3 = fieldset2.item(1);
        InputText name3 = item3.fields().field("name").control(InputText.class);
        name3.shouldHaveValue("AAA");
        // изменяем значение второго элемента и удаляем
        name2.val("BBB");
        item2.clickRemoveButton();
        fieldset2.shouldHaveItems(2);
        // проверяем значение третьего элемента, который стал вторым
        name2.shouldHaveValue("AAA");
    }

    @Test
    public void testNestedMultiFieldSet() {
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
        fieldset2.addButtonShouldBeExist();
        fieldset2.addButtonShouldHaveLabel("Добавить участника");
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        MultiFieldSetItem item1 = fieldset2.item(0);
        MultiFieldSetItem item2 = fieldset2.item(1);
        item1.shouldHaveLabel("Участник 1");
        item2.shouldHaveLabel("Участник 2");
        item1.removeButtonShouldNotExists();
        item1.copyButtonShouldExists();
        item2.removeButtonShouldExists();
        item2.copyButtonShouldExists();
        InputText name2 = item2.fields().field("name2").control(InputText.class);
        // copy
        name2.val("AAA");
        item2.copyButtonShouldExists();
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
        fieldset2.removeAllButtonShouldBeExist();
        fieldset2.removeAllButtonShouldHaveLabel("Удалить всех участников");
        fieldset2.clickRemoveAllButton();
        fieldset2.shouldHaveItems(1);
    }

    @Test
    public void testQueryData() {
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
        age3.val("30");
        // удаляем первый элемент и проверяем, что у третьего (теперь второго) не поменялись значения
        item1.clickRemoveButton();
        name2.shouldHaveValue("Joe");
        age2.shouldHaveValue("30");
    }

    @Test
    public void testDependencies() {
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
        age1.val("2");
        name1.shouldBeDisabled();
        name2.shouldBeDisabled();
        age2.val("20");
        name1.shouldBeDisabled();
        name2.shouldBeEnabled();
        age1.val("50");
        name1.shouldBeEnabled();
        name2.shouldBeEnabled();
        age2.val("15");
        name1.shouldBeEnabled();
        name2.shouldBeDisabled();
    }

    @Test
    public void testVisible() {
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

        inputText.val("test");
        multiSet1.shouldBeHidden();
        multiSet2.shouldBeVisible();
        multiSet2.addButtonShouldBeExist();
    }

    @Test
    public void testEnabled() {
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

        inputText.val("test");
        multiSet1.addButtonShouldBeDisabled();
        multiSet2.addButtonShouldBeEnabled();
    }

    @Test
    public void testModalToModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/modal_to_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/modal_to_modal/update.page.xml"));

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
        idCopy.shouldNotHaveValue(id.getValue());

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/filtering/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/filtering/test.query.xml"));

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
        inputSelect.dropdown().item(0).shouldHaveValue("test1");

        inputText.val("2");
        inputSelect.click();
        inputSelect.dropdown().shouldExists();
        inputSelect.dropdown().shouldHaveItems(1);
        inputSelect.dropdown().item(0).shouldHaveValue("test2");
    }

    @Test
    public void testValidation() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/validation/index.page.xml"));

        Configuration.headless=false;
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

        surnameSecond.shouldHaveValidationMessage(Condition.empty);
        nameSecond.shouldHaveValidationMessage(Condition.empty);
        ageSecond.shouldHaveValidationMessage(Condition.empty);

        validateBtn.click();

        surnameSecond.control(InputText.class).val("1");
        nameSecond.control(InputText.class).val("1");
        ageSecond.control(InputText.class).val("1");

        surnameSecond.shouldHaveValidationMessage(Condition.empty);
        nameSecond.shouldHaveValidationMessage(Condition.empty);
        ageSecond.shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    public void testCreateMany() {
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
        inputSelect.selectMulti(2, 3);
        modal.toolbar().bottomRight().button("Добавить").click();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset.shouldHaveItems(2);
        fieldset.item(0).fields().field("Имя").control(OutputText.class).shouldHaveValue("test3");
        fieldset.item(1).fields().field("Имя").control(OutputText.class).shouldHaveValue("test4");
        fieldset.item(1).fields().field("trash", ButtonField.class).click();
        fieldset.item(1).shouldNotExists();
    }

    @Test
    public void setRowIndexInFields() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/set_row_index/index.page.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset = page.widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);

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
}
