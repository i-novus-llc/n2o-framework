package net.n2oapp.demo.model;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static net.n2oapp.demo.model.BasePage.*;
import static net.n2oapp.demo.model.BasePage.getRowElements;
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
        getMainTableHead().shouldBe(Condition.exist);
        getMainTableRows().shouldBe(CollectionCondition.sizeGreaterThan(0));
        getMainTableFilter().shouldBe(Condition.exist);

        getTableHeaderSurname().shouldBe(Condition.exist);
        getFilterSearchButton().shouldBe(Condition.exist);
        getRegions().shouldHaveSize(3);
    }

    /**
     * Проверка работы фильтра по полу
     */
    public void testFilterByGender() {
        getCheckbox(getMainTableFilter(), "Женский").shouldBe(Condition.enabled).click();
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
     * Проверка работы сортировки по фамилии
     */
    public void testTableSorting() {
        getTableHeaderSurname().shouldBe(Condition.enabled).click();
        assertThat(isSorted(getColElements(getMainTable().shouldBe(Condition.exist), 1).texts(), true), is(true));

        getTableHeaderSurname().click();
        assertThat(isSorted(getColElements(getMainTable().shouldBe(Condition.exist), 1).texts(), false), is(true));

        getTableHeaderSurname().click();
        List<String> list = getColElements(getMainTable().shouldBe(Condition.exist), 1).texts();
        assertThat(isSorted(list, true), is(false));
        assertThat(isSorted(list, false), is(false));
    }

    /**
     * Проверка редактирования даты в таблице
     */
    public void testTableEditBirthday() {
        String testDate = "03.03.2020";

        getInput(getMainTableFilter(), "Фамилия").shouldBe(Condition.exist).setValue("Плюхина");
        getFilterSearchButton().click();
        String exDate = getRowElements(getMainTable(), 0).get(3).shouldBe(Condition.exist).text();
        getRowElements(getMainTable(), 0).get(3).click();

        getDatePicker(getRowElements(getMainTable(), 0).get(3)).shouldBe(Condition.exist);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).sendKeys(Keys.chord(Keys.CONTROL, "a"), testDate);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).pressEnter();
        getRowElements(getMainTable(), 0).get(3).shouldBe(Condition.text(testDate));

        getRowElements(getMainTable(), 0).get(3).click();
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).shouldBe(Condition.exist);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).sendKeys(Keys.chord(Keys.CONTROL, "a"), exDate);
        getDatePicker(getRowElements(getMainTable(), 0).get(3)).pressEnter();
        getRowElements(getMainTable(), 0).get(3).shouldBe(Condition.text(exDate));
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

        getInput(mainPage, "Отчество").shouldHave(Condition.value("Тест"));
        getInput(mainPage, "Фамилия").setValue("Иванов");
        getInput(mainPage, "Имя").setValue("Алексей");
        getInput(mainPage, "Отчество").setValue("Петрович");
        getRadioButton(mainPage, "Мужской").click();
        getInputDate(mainPage, "Дата рождения").setValue("17.01.2020");
        getCheckbox(mainPage, "VIP").click(-10, 0);
        getButton(mainPage, "Сохранить").click();


        getMainTablePaginationButton(0).shouldHave(Condition.cssClass("active"));
        getRowElements($(".n2o-page-body"), 0).get(0).parent().parent().shouldHave(Condition.cssClass("table-active"));

        getRowElements(mainPage, 0).get(0).shouldHave(Condition.text("Иванов"));
        getRowElements(mainPage, 0).get(1).shouldHave(Condition.text("Алексей"));
        getRowElements(mainPage, 0).get(2).shouldHave(Condition.text("Петрович"));
        getRowElements(mainPage, 0).get(3).shouldHave(Condition.text("17.01.2020"));
        getRowElements(mainPage, 0).get(4).shouldHave(Condition.text("Мужской"));
        getRowElements(mainPage, 0).get(5).$("input").shouldHave(Condition.attribute("checked"));
    }

    /**
     * Проверка создания клиента через модальное окно
     */
    public void assertCreateClient() {
        SelenideElement mainPage = getPage();
        getButton(mainPage, "Создать").click();

        SelenideElement modalPage = getModalPage();

        getInput(modalPage, "Отчество").shouldHave(Condition.value("Тест"));
        getInput(modalPage, "Фамилия").setValue("Иванов");
        getInput(modalPage, "Имя").setValue("Алексей");
        getInput(modalPage, "Отчество").setValue("Петрович");
        getRadioButton(modalPage, "Мужской").click();
        getInputDate(modalPage, "Дата рождения").setValue("17.01.2020");
        getCheckbox(modalPage, "VIP").click(-10, 0);
        getButton(modalPage, "Сохранить").click();

        getButton(getPage(), "Закрыть").click();

        getMainTablePaginationButton(0).shouldHave(Condition.cssClass("active"));
        getRowElements(mainPage, 0).get(0).parent().parent().shouldHave(Condition.cssClass("table-active"));

        getRowElements(mainPage, 0).get(0).shouldHave(Condition.text("Иванов"));
        getRowElements(mainPage, 0).get(1).shouldHave(Condition.text("Алексей"));
        getRowElements(mainPage, 0).get(2).shouldHave(Condition.text("Петрович"));
        getRowElements(mainPage, 0).get(3).shouldHave(Condition.text("17.01.2020"));
        getRowElements(mainPage, 0).get(4).shouldHave(Condition.text("Мужской"));
        getRowElements(mainPage, 0).get(5).$("input").shouldHave(Condition.attribute("checked"));
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
        getInput(modalPage, "Фамилия").shouldHave(Condition.value(surname));
        getInput(modalPage, "Имя").shouldHave(Condition.value(name));
        getInput(modalPage, "Отчество").shouldHave(Condition.value(patronymic));
        getRadioButton(modalPage, gender).shouldHave(Condition.cssClass("checked"));
        getInputDate(modalPage, "Дата рождения").shouldHave(Condition.value(birthDate));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(Condition.attribute("value", vip));

        getInput(modalPage, "Фамилия").setValue("Иванов");
        getInput(modalPage, "Имя").setValue("Алексей");
        getInput(modalPage, "Отчество").setValue("Петрович");
        getButton(modalPage, "Сохранить").click();

        getMainTablePaginationButton(0).shouldHave(Condition.cssClass("active"));
        getRowElements(mainPage, 1).get(0).parent().parent().shouldHave(Condition.cssClass("table-active"));

        getRowElements(mainPage, 1).get(0).shouldHave(Condition.text("Иванов"));
        getRowElements(mainPage, 1).get(1).shouldHave(Condition.text("Алексей"));
        getRowElements(mainPage, 1).get(2).shouldHave(Condition.text("Петрович"));
        getRowElements(mainPage, 1).get(3).shouldHave(Condition.text(birthDate));
        getRowElements(mainPage, 1).get(4).shouldHave(Condition.text(gender));
        getRowElements(mainPage, 1).get(5).$("input")
                .shouldHave("true".equals(vip) ? Condition.attribute("checked") : Condition.not(Condition.attribute("checked")));

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
        getInput(modalPage, "Фамилия").shouldHave(Condition.value(surname));
        getInput(modalPage, "Фамилия").shouldHave(Condition.attribute("disabled"));
        getInput(modalPage, "Имя").shouldHave(Condition.value(name));
        getInput(modalPage, "Имя").shouldHave(Condition.attribute("disabled"));
        getInput(modalPage, "Отчество").shouldHave(Condition.value(patronymic));
        getInput(modalPage, "Отчество").shouldHave(Condition.attribute("disabled"));
        getInputSelect(modalPage, "Пол").shouldHave(Condition.text(gender));
        getInputSelect(modalPage, "Пол").$(".form-control").shouldHave(Condition.cssClass("disabled"));
        getInputDate(modalPage, "Дата рождения").shouldHave(Condition.value(birthDate));
        getInputDate(modalPage, "Дата рождения").shouldHave(Condition.attribute("disabled"));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(Condition.attribute("value", vip));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(Condition.attribute("disabled"));
    }

    /**
     * Тест удаления клиента (предпоследняя строка) из тулбара в колонке
     */
    public void testTableInPlaceDelete() {
        List<String> row = getRowElements(getMainTable(), 8).shouldHaveSize(7).texts();
        List<String> nRow = getRowElements(getMainTable(), 9).shouldHaveSize(7).texts();

        String count = getMainTablePaginationInfo().should(Condition.exist).getText();
        Integer total = Integer.valueOf(count.split(" ")[1]);

        getButton(getRowElements(getMainTable(), 8).get(6), "").shouldBe(Condition.exist).click();
        getButton(getRowElements(getMainTable(), 8).get(6), "Удалить").shouldBe(Condition.exist).click();

        getModalDialog("Предупреждение").should(Condition.exist);
        getModalDialogBody("Предупреждение").should(Condition.matchText(row.get(0)+" "+row.get(1)));
        getButton(getModalDialog("Предупреждение"), "Да").click();
        getModalDialog("Предупреждение").shouldNot(Condition.exist);

        getAlerts().shouldHave(CollectionCondition.size(1)).get(0).should(Condition.matchText(row.get(0)));
        count = getMainTablePaginationInfo().should(Condition.exist).getText();
        assertThat(Integer.valueOf(count.split(" ")[1]), is(total - 1));

        List<String> aRow = getRowElements(getMainTable(), 8).shouldHaveSize(7).texts();
        assertThat(aRow.get(0).equals(row.get(0)) || aRow.get(1).equals(row.get(1)), is(false));
        assertThat(nRow.get(0).equals(aRow.get(0)) && nRow.get(1).equals(aRow.get(1)), is(true));
    }

    /**
     * Тест удаления клиента (предпоследняя строка) из тулбара таблицы
     */
    public void testTableRowDelete() {
        List<String> row = getRowElements(getMainTable(), 8).shouldHaveSize(7).texts();
        List<String> nRow = getRowElements(getMainTable(), 9).shouldHaveSize(7).texts();
        String count = getMainTablePaginationInfo().should(Condition.exist).getText();
        Integer total = Integer.valueOf(count.split(" ")[1]);

        getRowElements(getMainTable(), 8).get(4).click();
        getButton(getRegions().get(0), "Удалить").click();

        getModalDialog("Предупреждение").should(Condition.exist);
        getModalDialogBody("Предупреждение").should(Condition.matchText(row.get(0)+" "+row.get(1)));
        getButton(getModalDialog("Предупреждение"), "Да").click();
        getModalDialog("Предупреждение").shouldNot(Condition.exist);

        getAlerts().shouldHave(CollectionCondition.size(1)).get(0).should(Condition.matchText(row.get(0)));
        count = getMainTablePaginationInfo().should(Condition.exist).getText();
        assertThat(Integer.valueOf(count.split(" ")[1]), is(total - 1));

        List<String> aRow = getRowElements(getMainTable(), 8).shouldHaveSize(7).texts();
        assertThat(aRow.get(0).equals(row.get(0)) || aRow.get(1).equals(row.get(1)), is(false));
        assertThat(nRow.get(0).equals(aRow.get(0)) && nRow.get(1).equals(aRow.get(1)), is(true));
    }
}
