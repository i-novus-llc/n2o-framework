package net.n2oapp.framework.autotest.fieldset;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Автотест для проверки вложенного мультисета
 */
class NestedMultiFieldSetAT extends AutoTestBase {

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
    void checkIndexesInLabels() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/nested_multiset/indexes_in_labels/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        MultiFieldSet parentMultiFieldset = page.regions()
                .region(0, SimpleRegion.class)
                .content().widget(FormWidget.class)
                .fieldsets()
                .fieldset(0, MultiFieldSet.class);
        parentMultiFieldset.clickAddButton();

        // Проверка первого элемента вложенного мультисета первого родительского мультисета
        MultiFieldSet nestedMultiset = parentMultiFieldset.item(0).fieldsets().fieldset(0, MultiFieldSet.class);
        nestedMultiset.clickAddButton("Добавить участника");
        nestedMultiset.clickAddButton("Добавить участника");
        nestedMultiset.item(0).shouldHaveLabel("Лидер группы 1");
        nestedMultiset.item(1).shouldHaveLabel("Участник 1 группы 1");
        nestedMultiset.item(0).fields().field("label 1.0").control(InputText.class).shouldExists();
        nestedMultiset.item(1).fields().field("label 1.1").control(InputText.class).shouldExists();

        //проверка второго элемента родительского мультисета
        parentMultiFieldset.clickAddButton("Добавить группу");
        nestedMultiset = parentMultiFieldset.item(3).fieldsets().fieldset(0, MultiFieldSet.class);
        nestedMultiset.clickAddButton("Добавить участника");
        nestedMultiset.clickAddButton("Добавить участника");
        nestedMultiset.clickAddButton("Добавить участника");
        nestedMultiset.item(0).shouldHaveLabel("Лидер группы 2");
        nestedMultiset.item(1).shouldHaveLabel("Участник 1 группы 2");
        nestedMultiset.item(2).shouldHaveLabel("Участник 2 группы 2");
        nestedMultiset.item(0).fields().field("label 2.0").control(InputText.class).shouldExists();
        nestedMultiset.item(1).fields().field("label 2.1").control(InputText.class).shouldExists();
        nestedMultiset.item(2).fields().field("label 2.2").control(InputText.class).shouldExists();
    }

    @Test
    void checkDefaultValues() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/nested_multiset/default_values/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        MultiFieldSet parentMultiFieldset = page.regions()
                .region(0, SimpleRegion.class)
                .content().widget(FormWidget.class)
                .fieldsets()
                .fieldset(0, MultiFieldSet.class);
        parentMultiFieldset.clickAddButton();

        //проверка первого элемета род-го мультисета
        parentMultiFieldset.item(0).fields().field("index").control(InputText.class).shouldHaveValue("0");
        String parentItemZeroId = parentMultiFieldset.item(0).fields().field("id").control(InputText.class).getValue();
        parentMultiFieldset.item(0).fields().field("name").control(InputText.class).shouldHaveValue("parentTest");
        parentMultiFieldset.item(0).fields().field("type").control(Select.class).shouldHaveValue("parentType1");
        parentMultiFieldset.item(0).fields().field("date").control(DateInterval.class).beginShouldHaveValue("12.12.2024");
        parentMultiFieldset.item(0).fields().field("date").control(DateInterval.class).endShouldHaveValue("13.12.2024");

        MultiFieldSet nestedMultiset = parentMultiFieldset.item(0).fieldsets().fieldset(0, MultiFieldSet.class);
        //проверка первого элемента вложенного мультисета первого род-го мультисета
        nestedMultiset.clickAddButton("Добавить участника");

        nestedMultiset.item(0).fields().field("index").control(InputText.class).shouldHaveValue("0");
        String nestedChildItemZeroId = nestedMultiset.item(0).fields().field("id").control(InputText.class).getValue();
        nestedMultiset.item(0).fields().field("name").control(InputText.class).shouldHaveValue("childTest");
        nestedMultiset.item(0).fields().field("type").control(Select.class).shouldHaveValue("childType1");
        nestedMultiset.item(0).fields().field("date").control(DateInterval.class).beginShouldHaveValue("01.01.2024");
        nestedMultiset.item(0).fields().field("date").control(DateInterval.class).endShouldHaveValue("31.01.2024");

        //проверка второго элемента вложенного мультисета первого род-го мультисета
        nestedMultiset.clickAddButton("Добавить участника");

        nestedMultiset.item(1).fields().field("index").control(InputText.class).shouldHaveValue("1");
        String nestedChildItemOneId = nestedMultiset.item(1).fields().field("id").control(InputText.class).getValue();
        nestedMultiset.item(1).fields().field("name").control(InputText.class).shouldHaveValue("childTest");
        nestedMultiset.item(1).fields().field("type").control(Select.class).shouldHaveValue("childType1");
        nestedMultiset.item(1).fields().field("date").control(DateInterval.class).beginShouldHaveValue("01.01.2024");
        nestedMultiset.item(1).fields().field("date").control(DateInterval.class).endShouldHaveValue("31.01.2024");

        assertNotEquals(nestedChildItemZeroId, nestedChildItemOneId);

        //проверка второго элемета род-го мультисета
        parentMultiFieldset.clickAddButton("Добавить группу");

        parentMultiFieldset.item(3).fields().field("index").control(InputText.class).shouldHaveValue("1");
        String parentItemOneId = parentMultiFieldset.item(3).fields().field("id").control(InputText.class).getValue();
        parentMultiFieldset.item(3).fields().field("name").control(InputText.class).shouldHaveValue("parentTest");
        parentMultiFieldset.item(3).fields().field("type").control(Select.class).shouldHaveValue("parentType1");
        parentMultiFieldset.item(3).fields().field("date").control(DateInterval.class).beginShouldHaveValue("12.12.2024");
        parentMultiFieldset.item(3).fields().field("date").control(DateInterval.class).endShouldHaveValue("13.12.2024");

        assertNotEquals(parentItemOneId, parentItemZeroId);

        MultiFieldSet secondNestedMultiset = parentMultiFieldset.item(3).fieldsets().fieldset(0, MultiFieldSet.class);

        //проверка первого элемента вложенного мультисета второго элемента род-го мультисета
        secondNestedMultiset.clickAddButton("Добавить участника");

        secondNestedMultiset.item(0).fields().field("index").control(InputText.class).shouldHaveValue("0");
        String secondNestedChildItemZeroId = secondNestedMultiset.item(0).fields().field("id").control(InputText.class).getValue();
        secondNestedMultiset.item(0).fields().field("name").control(InputText.class).shouldHaveValue("childTest");
        secondNestedMultiset.item(0).fields().field("type").control(Select.class).shouldHaveValue("childType1");
        secondNestedMultiset.item(0).fields().field("date").control(DateInterval.class).beginShouldHaveValue("01.01.2024");
        secondNestedMultiset.item(0).fields().field("date").control(DateInterval.class).endShouldHaveValue("31.01.2024");

        //проверка первого элемента вложенного мультисета второго элемента род-го мультисета
        secondNestedMultiset.clickAddButton("Добавить участника");

        secondNestedMultiset.item(1).fields().field("index").control(InputText.class).shouldHaveValue("1");
        String secondNestedChildItemOneId = nestedMultiset.item(1).fields().field("id").control(InputText.class).getValue();
        secondNestedMultiset.item(1).fields().field("name").control(InputText.class).shouldHaveValue("childTest");
        secondNestedMultiset.item(1).fields().field("type").control(Select.class).shouldHaveValue("childType1");
        secondNestedMultiset.item(1).fields().field("date").control(DateInterval.class).beginShouldHaveValue("01.01.2024");
        secondNestedMultiset.item(1).fields().field("date").control(DateInterval.class).endShouldHaveValue("31.01.2024");

        assertNotEquals(secondNestedChildItemZeroId, secondNestedChildItemOneId);

        //проверка генерируемых id-шников элементов разных вложенных мультисетов
        assertNotEquals(nestedChildItemZeroId, secondNestedChildItemZeroId);
        assertNotEquals(nestedChildItemZeroId, secondNestedChildItemOneId);
    }

    @Test
    void checkDependencies() {
        testDependencies("net/n2oapp/framework/autotest/fieldset/nested_multiset/dependencies/index.page.xml");
    }

    @Test
    void checkConditions() {
        testDependencies("net/n2oapp/framework/autotest/fieldset/nested_multiset/conditions/index.page.xml");
    }

    private void testDependencies(String path) {
        builder.sources(new CompileInfo(path));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);

        MultiFieldSet parentSet = form.fieldsets().fieldset(1, MultiFieldSet.class);
        parentSet.clickAddButton();
        MultiFieldSet childSet = parentSet.item(0).fieldsets().fieldset(0, MultiFieldSet.class);
        childSet.clickAddButton("add child set");

        // Проверка зависимости от global чекбокса
        Checkbox globalCheckbox = form.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("global checkbox").control(Checkbox.class);
        InputText parentInput1 = parentSet.item(0).fields().field("on global").control(InputText.class);
        InputText childInput1 = childSet.item(0).fields().field("on global").control(InputText.class);
        globalCheckbox.shouldNotBeChecked();
        parentInput1.shouldBeEnabled();
        childInput1.shouldBeEnabled();
        globalCheckbox.setChecked(true);
        parentInput1.shouldBeDisabled();
        childInput1.shouldBeDisabled();

        // Проверка зависимости от parent чекбокса
        Checkbox parentCheckbox = parentSet.item(0).fields().field("parent checkbox").control(Checkbox.class);
        InputText parentInput2 = parentSet.item(0).fields().field("on parent").control(InputText.class);
        InputText childInput2 = childSet.item(0).fields().field("on parent").control(InputText.class);
        parentCheckbox.shouldNotBeChecked();
        parentInput2.shouldBeEnabled();
        childInput2.shouldBeEnabled();
        parentCheckbox.setChecked(true);
        parentInput2.shouldBeDisabled();
        childInput2.shouldBeDisabled();

        // Проверка зависимости от child чекбокса
        Checkbox childCheckbox = childSet.item(0).fields().field("child checkbox").control(Checkbox.class);
        InputText childInput3 = childSet.item(0).fields().field("on child").control(InputText.class);
        childCheckbox.shouldNotBeChecked();
        childInput3.shouldBeEnabled();
        childCheckbox.setChecked(true);
        childInput3.shouldBeDisabled();
    }

    @Test
    void checkValidations() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/nested_multiset/validations/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        MultiFieldSet parentMultiFieldset = page.regions()
                .region(0, SimpleRegion.class)
                .content().widget(FormWidget.class)
                .fieldsets()
                .fieldset(1, MultiFieldSet.class);
        Checkbox checkbox = page.regions()
                .region(0, SimpleRegion.class)
                .content().widget(FormWidget.class)
                .fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields().field("check").control(Checkbox.class);
        StandardButton validate = page.toolbar().topLeft().button("Validate");

        //Добавляем 2 группы по 2 участника
        parentMultiFieldset.clickAddButton("Добавить группу");
        parentMultiFieldset.clickAddButton("Добавить участника");
        parentMultiFieldset.clickAddButton("Добавить участника");

        MultiFieldSet nestedFieldsetFirst = parentMultiFieldset.item(0).fieldsets().fieldset(0, MultiFieldSet.class);

        parentMultiFieldset.clickAddButton("Добавить группу");

        MultiFieldSet nestedFieldsetSecond = parentMultiFieldset.item(3).fieldsets().fieldset(0, MultiFieldSet.class);
        nestedFieldsetSecond.clickAddButton("Добавить участника");
        nestedFieldsetSecond.clickAddButton("Добавить участника");

        //Проверяем, что все чекбоксы не выбраны
        checkboxesShouldNotBeChecked(parentMultiFieldset, nestedFieldsetFirst, nestedFieldsetSecond);

        //валидируем
        validate.click();

        //проверяем, что все поля не прошли валидацию
        checkAllFieldsShouldHaveValidationMessage(parentMultiFieldset, nestedFieldsetFirst, nestedFieldsetSecond);

        //выбираем внешний чекбокс и валидируем
        checkbox.setChecked(true);
        validate.click();

        //валидацию должны пройти все поля, зависимые от внешнего поля
        parentMultiFieldset.item(0).fields().field("field1").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetFirst.item(0).fields().field("field1").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetFirst.item(1).fields().field("field1").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        parentMultiFieldset.item(3).fields().field("field1").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetSecond.item(0).fields().field("field1").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetSecond.item(1).fields().field("field1").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));

        //выбираем чекбокс в первой группе и проверяем
        parentMultiFieldset.item(0).fields().field("firstLevelCheck").control(Checkbox.class).setChecked(true);
        validate.click();

        //прошли проверку все поля из первой группы зависимые от этого чекбокса, поля из второй группы не прошли валидацию
        parentMultiFieldset.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetFirst.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetFirst.item(1).fields().field("field2").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        parentMultiFieldset.item(3).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(1).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));

        //выбираем чекбокс во второй группе и проверяем
        parentMultiFieldset.item(3).fields().field("firstLevelCheck").control(Checkbox.class).setChecked(true);
        validate.click();

        //прошли проверку все поля из второй группы зависимые от этого чекбокса
        parentMultiFieldset.item(3).fields().field("field2").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetSecond.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetSecond.item(1).fields().field("field2").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));

        //выбираем чекбокс в первом участнике первой группы и проверяем
        nestedFieldsetFirst.item(0).fields().field("secondLevelCheck").control(Checkbox.class).setChecked(true);
        validate.click();

        //поле из первого участника первой группы прошло валидацию, все остальные поля участников не прошли
        nestedFieldsetFirst.item(0).fields().field("field3").shouldHaveValidationMessage(Condition.not(Condition.text("Заполните поле")));
        nestedFieldsetFirst.item(1).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(0).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(1).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));
    }

    private static void checkboxesShouldNotBeChecked(MultiFieldSet parentMultiFieldset, MultiFieldSet nestedFieldsetFirst, MultiFieldSet nestedFieldsetSecond) {
        parentMultiFieldset.item(0).fields().field("firstLevelCheck").control(Checkbox.class).shouldNotBeChecked();
        nestedFieldsetFirst.item(0).fields().field("secondLevelCheck").control(Checkbox.class).shouldNotBeChecked();
        nestedFieldsetFirst.item(1).fields().field("secondLevelCheck").control(Checkbox.class).shouldNotBeChecked();

        parentMultiFieldset.item(3).fields().field("firstLevelCheck").control(Checkbox.class).shouldNotBeChecked();
        nestedFieldsetSecond.item(0).fields().field("secondLevelCheck").control(Checkbox.class).shouldNotBeChecked();
        nestedFieldsetSecond.item(1).fields().field("secondLevelCheck").control(Checkbox.class).shouldNotBeChecked();
    }

    private static void checkAllFieldsShouldHaveValidationMessage(MultiFieldSet parentMultiFieldset, MultiFieldSet nestedFieldsetFirst, MultiFieldSet nestedFieldsetSecond) {
        parentMultiFieldset.item(0).fields().field("field1").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        parentMultiFieldset.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetFirst.item(0).fields().field("field1").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetFirst.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetFirst.item(0).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetFirst.item(1).fields().field("field1").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetFirst.item(1).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetFirst.item(1).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));

        parentMultiFieldset.item(3).fields().field("field1").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        parentMultiFieldset.item(3).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(0).fields().field("field1").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(0).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(0).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(1).fields().field("field1").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(1).fields().field("field2").shouldHaveValidationMessage(Condition.text("Заполните поле"));
        nestedFieldsetSecond.item(1).fields().field("field3").shouldHaveValidationMessage(Condition.text("Заполните поле"));
    }
}
