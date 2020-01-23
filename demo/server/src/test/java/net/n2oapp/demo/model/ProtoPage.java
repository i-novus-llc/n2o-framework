package net.n2oapp.demo.model;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static net.n2oapp.demo.model.BasePage.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Тесты ProtoPage
 */
public class ProtoPage implements ProtoPageSelectors {

    /**
     * Проверка правильности селекторов
     */
    public void checkAllElementsExists() {
        getMainTableHead().shouldBe(exist);
        getMainTableRows().shouldBe(CollectionCondition.sizeGreaterThan(0));
        getMainTableFilter().shouldBe(exist);

        getTableHeaderSurname().shouldBe(exist);
        getFilterSearchButton().shouldBe(exist);
    }

    /**
     * Проверка работы фильтра по полу
     */
    public void testFilterByGender() {
        getCheckbox(getMainTableFilter(), "Женский").shouldBe(enabled).click();
        getFilterSearchButton().click();
        getColElements(getMainTable(), 5).shouldHave(CollectionCondition.texts(Collections.nCopies(10, "Женский")));

        getCheckbox(getMainTableFilter(), "Женский").click();
        getCheckbox(getMainTableFilter(), "Мужской").click();
        getFilterSearchButton().click();
        getColElements(getMainTable(), 5).shouldHave(CollectionCondition.texts(Collections.nCopies(10, "Мужской")));

        getCheckbox(getMainTableFilter(), "Мужской").click();
        getCheckbox(getMainTableFilter(), "Не определенный").click();
        getFilterSearchButton().click();
        getMainTableRows().shouldHave(CollectionCondition.empty);
    }

    /**
     * Проверка работы очистки фильтра
     */
    public void testClearFilter() {
        getInput(getMainTableFilter(), "Имя").val("Римма");
        getCheckbox(getMainTableFilter(), "Женский").click();
        getCheckbox(getMainTableFilter(), "VIP").click(-10, 0);

        getFilterSearchButton().click();
        getMainTableRows().shouldHaveSize(2);

        getFilterResetButton().click();
        getMainTableRows().shouldHaveSize(10);

        getInput(getMainTableFilter(), "Имя").shouldHave(value(""));
        getCheckboxInput(getMainTableFilter(), "Женский").shouldNotBe(checked);
        getCheckboxInput(getMainTableFilter(), "VIP").shouldNotBe(checked);
    }

    /**
     * Проверка работы сортировки по фамилии
     */
    public void testTableSorting() {
        getTableHeaderSurname().shouldBe(enabled).click();
        assertThat(isSorted(getColElements(getMainTable().shouldBe(exist), 1).texts(), true), is(true));

        getTableHeaderSurname().click();
        assertThat(isSorted(getColElements(getMainTable().shouldBe(exist), 1).texts(), false), is(true));

        getTableHeaderSurname().click();
        List<String> list = getColElements(getMainTable().shouldBe(exist), 1).texts();
        assertThat(isSorted(list, true), is(false));
        assertThat(isSorted(list, false), is(false));
    }

    /**
     * Проверка редактирования даты в таблице
     */
    public void testTableEditBirthday() {
        String testDate = "03.03.2020";

        getInput(getMainTableFilter(), "Фамилия").shouldBe(exist).setValue("Плюхина");
        getFilterSearchButton().click();
        String exDate = getRowElements(getMainTable(), 0).get(3).shouldBe(exist).text();
        getRowElements(getMainTable(), 0).get(3).click();

        getDatePicker(getRowElements(getMainTable(), 0).get(3)).shouldBe(exist);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).sendKeys(Keys.chord(Keys.CONTROL, "a"), testDate);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).pressEnter();
        getRowElements(getMainTable(), 0).get(3).shouldBe(text(testDate));

        getRowElements(getMainTable(), 0).get(3).click();
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).shouldBe(exist);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).sendKeys(Keys.chord(Keys.CONTROL, "a"), exDate);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).pressEnter();
        getRowElements(getMainTable(), 0).get(3).shouldBe(text(exDate));
    }


    private boolean isSorted(List<String> list, Boolean dir) {
        for (int i = 0; i < list.size() - 1; i++) {
            if ((dir && list.get(i).compareTo(list.get(i + 1)) >= 0)
                    || (!dir && list.get(i).compareTo(list.get(i + 1)) <= 0)) return false;
        }
        return true;
    }

    /**
     * Проверка создания клиента
     */
    public void assertAddClient() {
        SelenideElement mainPage = getPage();
        getButton(mainPage, "Добавить клиента").click();

        getInput(mainPage, "Отчество").shouldHave(value("Тест"));
        getInput(mainPage, "Фамилия").setValue("Иванов");
        getInput(mainPage, "Имя").setValue("Алексей");
        getInput(mainPage, "Отчество").setValue("Петрович");
        getRadioButton(mainPage, "Мужской").click();
        getInputDate(mainPage, "Дата рождения").setValue("17.01.2020");
        getCheckbox(mainPage, "VIP").click(-10, 0);
        getButton(mainPage, "Сохранить").click();


        getMainTablePaginationButton(0).shouldHave(cssClass("active"));
        getRowElements($(".n2o-page-body"), 0).get(0).parent().parent().shouldHave(cssClass("table-active"));

        getRowElements(mainPage, 0).get(0).shouldHave(text("Иванов"));
        getRowElements(mainPage, 0).get(1).shouldHave(text("Алексей"));
        getRowElements(mainPage, 0).get(2).shouldHave(text("Петрович"));
        getRowElements(mainPage, 0).get(3).shouldHave(text("17.01.2020"));
        getRowElements(mainPage, 0).get(4).shouldHave(text("Мужской"));
        getRowElements(mainPage, 0).get(5).$("input").shouldHave(attribute("checked"));
    }

    /**
     * Проверка создания клиента через модальное окно
     */
    public void assertCreateClient() {
        SelenideElement mainPage = getPage();
        getButton(mainPage, "Создать").click();

        SelenideElement modalPage = getModalPage();

        getInput(modalPage, "Отчество").shouldHave(value("Тест"));
        getInput(modalPage, "Фамилия").setValue("Иванов");
        getInput(modalPage, "Имя").setValue("Алексей");
        getInput(modalPage, "Отчество").setValue("Петрович");
        getRadioButton(modalPage, "Мужской").click();
        getInputDate(modalPage, "Дата рождения").setValue("17.01.2020");
        getCheckbox(modalPage, "VIP").click(-10, 0);
        getButton(modalPage, "Сохранить").click();

        getButton(getPage(), "Закрыть").click();

        getMainTablePaginationButton(0).shouldHave(cssClass("active"));
        getRowElements(mainPage, 0).get(0).parent().parent().shouldHave(cssClass("table-active"));

        getRowElements(mainPage, 0).get(0).shouldHave(text("Иванов"));
        getRowElements(mainPage, 0).get(1).shouldHave(text("Алексей"));
        getRowElements(mainPage, 0).get(2).shouldHave(text("Петрович"));
        getRowElements(mainPage, 0).get(3).shouldHave(text("17.01.2020"));
        getRowElements(mainPage, 0).get(4).shouldHave(text("Мужской"));
        getRowElements(mainPage, 0).get(5).$("input").shouldHave(attribute("checked"));
    }

    /**
     * Проверка изменения клиента через модальное окно
     */
    public void assertUpdateClient() {
        SelenideElement mainPage = getPage();
        List<String> row = getRow(getMainTableRows(), 1);

        String surname = row.get(0);
        String name = row.get(1);
        String patronymic = row.get(2);
        String birthDate = row.get(3);
        String gender = row.get(4);
        String vip = "true".equals(row.get(5)) ? "true" : "false";

        getRowElements(mainPage, 1).get(4).click();
        getButton(mainPage, "Изменить").click();

        SelenideElement modalPage = getModalPage();
        getInput(modalPage, "Фамилия").shouldHave(value(surname));
        getInput(modalPage, "Имя").shouldHave(value(name));
        getInput(modalPage, "Отчество").shouldHave(value(patronymic));
        getRadioButton(modalPage, gender).shouldHave(cssClass("checked"));
        getInputDate(modalPage, "Дата рождения").shouldHave(value(birthDate));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(attribute("value", vip));

        getInput(modalPage, "Фамилия").setValue("Иванов");
        getInput(modalPage, "Имя").setValue("Алексей");
        getInput(modalPage, "Отчество").setValue("Петрович");
        getButton(modalPage, "Сохранить").click();

        getMainTablePaginationButton(0).shouldHave(cssClass("active"));
        getRowElements(mainPage, 1).get(0).parent().parent().shouldHave(cssClass("table-active"));

        getRowElements(mainPage, 1).get(0).shouldHave(text("Иванов"));
        getRowElements(mainPage, 1).get(1).shouldHave(text("Алексей"));
        getRowElements(mainPage, 1).get(2).shouldHave(text("Петрович"));
        getRowElements(mainPage, 1).get(3).shouldHave(text(birthDate));
        getRowElements(mainPage, 1).get(4).shouldHave(text(gender));
        getRowElements(mainPage, 1).get(5).$("input")
                .shouldHave("true".equals(vip) ? attribute("checked") : not(attribute("checked")));

    }

    /**
     * Просмотр клиента через модальное окно
     */
    public void assertViewClient() {
        List<String> row = getRow(getMainTableRows(), 1);
        String surname = row.get(0);
        String name = row.get(1);
        String patronymic = row.get(2);
        String birthDate = row.get(3);
        String gender = row.get(4);
        String vip = "true".equals(row.get(5)) ? "true" : "false";

        SelenideElement mainPage = getPage();
        getRowElements(mainPage, 1).get(4).click();
        getButton(mainPage, "Просмотр").click();

        SelenideElement modalPage = getModalPage();
        getInput(modalPage, "Фамилия").shouldHave(value(surname));
        getInput(modalPage, "Фамилия").shouldHave(attribute("disabled"));
        getInput(modalPage, "Имя").shouldHave(value(name));
        getInput(modalPage, "Имя").shouldHave(attribute("disabled"));
        getInput(modalPage, "Отчество").shouldHave(value(patronymic));
        getInput(modalPage, "Отчество").shouldHave(attribute("disabled"));
        getInputSelect(modalPage, "Пол").shouldHave(text(gender));
        getInputSelect(modalPage, "Пол").$(".form-control").shouldHave(cssClass("disabled"));
        getInputDate(modalPage, "Дата рождения").shouldHave(value(birthDate));
        getInputDate(modalPage, "Дата рождения").shouldHave(attribute("disabled"));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(attribute("value", vip));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(attribute("disabled"));
    }

}
