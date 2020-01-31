package net.n2oapp.demo.model;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
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
     * Проверка работы фильтра по фамилии и имени
     */
    public void testFilterByNameAndSurname() {
        getInput(getMainTableFilter(), "Фамилия").val("Лапа");
        getInput(getMainTableFilter(), "Имя").val("ера");
        getFilterSearchButton().click();

        getMainTableRows().shouldHaveSize(1);
        getRowElements(getMainTable(), 0).get(0).shouldHave(Condition.text("Лапаева"));
        getRowElements(getMainTable(), 0).get(1).shouldHave(Condition.text("Вера"));
    }

    /**
     * Проверка работы фильтра по дате рождения
     */
    public void testFilterByBirthday() {
        getDateIntervalStart(getMainTableFilter(), "Дата рождения").val("01.01.1940");
        getDateIntervalEnd(getMainTableFilter(), "Дата рождения").val("01.12.1940");
        getFilterSearchButton().click();

        getMainTableRows().shouldHaveSize(2);
        getRowElements(getMainTable(), 0).get(0).shouldHave(Condition.text("Кручинина"));
        getRowElements(getMainTable(), 1).get(0).shouldHave(Condition.text("Мишин"));
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

        getInput(getMainTableFilter(), "Имя").shouldHave(Condition.value(""));
        getCheckbox(getMainTableFilter(), "Женский").$("input").shouldNotBe(Condition.checked);
        getCheckbox(getMainTableFilter(), "VIP").$("input").shouldNotBe(Condition.checked);
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
     * Проверка работы ячейки Фамилия
     */
    public void testSurnameCell() {
        getRowElements(getMainTable(), 4).get(0).$(".btn").click();
        SelenideElement openPage = getPage();
        getBreadcrumbActiveItem().shouldHave(Condition.text("Карточка клиента: "));

        getInput(openPage, "Фамилия").shouldHave(Condition.value("Вольваков"));
        getInput(openPage, "Имя").shouldHave(Condition.value("Вениамин"));
        getInput(openPage, "Отчество").shouldHave(Condition.value("Тихонович"));
        getRadioButton(openPage, "Мужской").$("input").shouldBe(Condition.checked);
        getInputDate(openPage, "Дата рождения").shouldHave(Condition.value("04.06.1932"));
        getCheckbox(openPage, "VIP").$("input").shouldBe(Condition.checked);

        getInput(openPage, "Фамилия").setValue("Сергеев");
        getInput(openPage, "Имя").setValue("Николай");
        getInput(openPage, "Отчество").setValue("Петрович");
        getButton(openPage, "Сохранить").click();

        getMainTablePaginationActiveButton().shouldHave(Condition.text("1"));
        getRowElements(getMainTable(), 4).get(0).shouldHave(Condition.text("Сергеев"));
        getRowElements(getMainTable(), 4).get(1).shouldHave(Condition.text("Николай"));
        getRowElements(getMainTable(), 4).get(2).shouldHave(Condition.text("Петрович"));
    }

    /**
     * Проверка работы ячейки Имя
     */
    public void testNameCell() {
        getRowElements(getMainTable(), 5).get(1).$(".btn").click();
        SelenideElement modalPage = getModalPage().shouldBe(Condition.exist);

        getInput(modalPage, "Фамилия").shouldHave(Condition.value("Дуванова"));
        getInput(modalPage, "Имя").shouldHave(Condition.value("Ольга"));
        getInput(modalPage, "Отчество").shouldHave(Condition.value("Юлиевна"));
        getRadioButton(modalPage, "Женский").$("input").shouldBe(Condition.checked);
        getInputDate(modalPage, "Дата рождения").shouldHave(Condition.value("02.09.1932"));
        getCheckbox(modalPage, "VIP").$("input").shouldBe(Condition.checked);

        getInput(modalPage, "Фамилия").setValue("Григорьева");
        getInput(modalPage, "Имя").setValue("Александра");
        getInput(modalPage, "Отчество").setValue("Петровна");
        getButton(modalPage, "Сохранить").click();

        getMainTablePaginationActiveButton().shouldHave(Condition.text("1"));
        getRowElements(getMainTable(), 5).get(0).shouldHave(Condition.text("Григорьева"));
        getRowElements(getMainTable(), 5).get(1).shouldHave(Condition.text("Александра"));
        getRowElements(getMainTable(), 5).get(2).shouldHave(Condition.text("Петровна"));
    }

    /**
     * Проверка работы ячейки Отчество
     */
    public void testPatronymicCell() {
        getRowElements(getMainTable(), 7).get(2).$(".btn").click();
        SelenideElement openPage = getPage();
        getBreadcrumbActiveItem().shouldHave(Condition.text("Карточка клиента: "));

        getInput(openPage, "Фамилия").shouldHave(Condition.value("Иванова"));
        getInput(openPage, "Имя").shouldHave(Condition.value("Зинаида"));
        getInput(openPage, "Отчество").shouldHave(Condition.value("Виталиевна"));
        getRadioButton(openPage, "Женский").$("input").shouldBe(Condition.checked);
        getInputDate(openPage, "Дата рождения").shouldHave(Condition.value("11.09.1933"));
        getCheckbox(openPage, "VIP").$("input").shouldBe(Condition.checked);

        getInput(openPage, "Фамилия").setValue("Сергеева");
        getInput(openPage, "Имя").setValue("Анастасия");
        getInput(openPage, "Отчество").setValue("Михайловна");
        getButton(openPage, "Сохранить").click();

        getMainTablePaginationActiveButton().shouldHave(Condition.text("1"));
        getRowElements(getMainTable(), 7).get(0).shouldHave(Condition.text("Сергеева"));
        getRowElements(getMainTable(), 7).get(1).shouldHave(Condition.text("Анастасия"));
        getRowElements(getMainTable(), 7).get(2).shouldHave(Condition.text("Михайловна"));
    }

    /**
     * Проверка работы ячейки VIP
     */
    public void testVipCell() {
        getRowElements(getMainTable(), 2).get(5).$("input").shouldBe(Condition.checked);
        getRowElements(getMainTable(), 2).get(5).click(-10, 0);
        getRowElements(getMainTable(), 2).get(5).$("input").shouldNotBe(Condition.checked);
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
        getRowElements(mainPage, 0).get(5).$("input").shouldBe(Condition.checked);
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
        getRowElements(mainPage, 0).get(5).$("input").shouldBe(Condition.checked);
    }

    /**
     * Проверка изменения клиента через модальное окно
     */
    public void assertUpdateClient() {
        SelenideElement mainPage = getPage();
        List<String> row = getRow(getMainTableRows(), 2);

        String surname = row.get(0);
        String name = row.get(1);
        String patronymic = row.get(2);
        String birthDate = row.get(3);
        String gender = row.get(4);
        String vip = "true".equals(row.get(5)) ? "true" : "false";

        getRowElements(mainPage, 2).get(4).click();
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
        getRowElements(mainPage, 2).get(0).parent().parent().shouldHave(Condition.cssClass("table-active"));

        getRowElements(mainPage, 2).get(0).shouldHave(Condition.text("Иванов"));
        getRowElements(mainPage, 2).get(1).shouldHave(Condition.text("Алексей"));
        getRowElements(mainPage, 2).get(2).shouldHave(Condition.text("Петрович"));
        getRowElements(mainPage, 2).get(3).shouldHave(Condition.text(birthDate));
        getRowElements(mainPage, 2).get(4).shouldHave(Condition.text(gender));
        getRowElements(mainPage, 2).get(5).$("input")
                .shouldBe("true".equals(vip) ? Condition.checked : Condition.not(Condition.checked));

    }

    /**
     * Проверка изменения клиента через тулбар ячейку
     */
    public void assertUpdateClientFromToolbarCell() {
        SelenideElement mainPage = getPage();
        List<String> row = getRow(getMainTableRows(), 2);

        String surname = row.get(0);
        String name = row.get(1);
        String patronymic = row.get(2);
        String birthDate = row.get(3);
        String gender = row.get(4);
        String vip = "true".equals(row.get(5)) ? "true" : "false";

        ElementsCollection rowElements = getRowElements(mainPage, 2);
        rowElements.get(4).click();
        rowElements.get(6).click();
        getButton(rowElements.get(6), "Изменить").click();

        SelenideElement modalPage = getModalPage();
        getInput(modalPage, "Фамилия").shouldHave(Condition.value(surname));
        getInput(modalPage, "Имя").shouldHave(Condition.value(name));
        getInput(modalPage, "Отчество").shouldHave(Condition.value(patronymic));
        getRadioButton(modalPage, gender).shouldHave(Condition.cssClass("checked"));
        getInputDate(modalPage, "Дата рождения").shouldHave(Condition.value(birthDate));
        getCheckbox(modalPage, "VIP").$("input").shouldHave(Condition.attribute("value", vip));

        getInput(modalPage, "Фамилия").setValue("Иванова");
        getInput(modalPage, "Имя").setValue("Наталья");
        getInput(modalPage, "Отчество").setValue("Петровна");
        getButton(modalPage, "Сохранить").click();

        getMainTablePaginationButton(0).shouldHave(Condition.cssClass("active"));

        rowElements = getRowElements(mainPage, 2);
        rowElements.get(0).parent().parent().shouldHave(Condition.cssClass("table-active"));
        rowElements.shouldHave(CollectionCondition.texts("Иванова", "Наталья", "Петровна", birthDate, gender, "", ""));
        rowElements.get(5).$("input")
                .shouldBe("true".equals(vip) ? Condition.checked : Condition.not(Condition.checked));
    }

    /**
     * Проверка изменения клиента через окно с бредкрампом
     */
    public void assertUpdateClientFromBreadcrumbPage() {
        SelenideElement mainPage = getPage();
        List<String> row = getRow(getMainTableRows(), 3);

        String surname = row.get(0);
        String name = row.get(1);
        String patronymic = row.get(2);
        String birthDate = row.get(3);
        String gender = row.get(4);
        String vip = "true".equals(row.get(5)) ? "true" : "false";

        ElementsCollection rowElements = getRowElements(mainPage, 3);
        rowElements.get(4).shouldBe(Condition.exist).click();
        SelenideElement editButton = mainPage.$(".fa-edit").shouldBe(Condition.exist);
        editButton.click();

        SelenideElement openPage = getPage();
        getInput(openPage, "Фамилия").shouldHave(Condition.value(surname));
        getInput(openPage, "Имя").shouldHave(Condition.value(name));
        getInput(openPage, "Отчество").shouldHave(Condition.value(patronymic));
        getRadioButton(openPage, gender).shouldHave(Condition.cssClass("checked"));
        getInputDate(openPage, "Дата рождения").shouldHave(Condition.value(birthDate));
        getCheckbox(openPage, "VIP").$("input").shouldHave(Condition.attribute("value", vip));

        getInput(openPage, "Фамилия").setValue("Иванова");
        getInput(openPage, "Имя").setValue("Наталья");
        getInput(openPage, "Отчество").setValue("Петровна");
        getButton(openPage, "Сохранить").click();

        getMainTablePaginationButton(0).shouldHave(Condition.cssClass("active"));

        rowElements = getRowElements(mainPage, 3);
        rowElements.get(0).parent().parent().shouldHave(Condition.cssClass("table-active"));
        rowElements.shouldHave(CollectionCondition.texts("Иванова", "Наталья", "Петровна", birthDate, gender, "", ""));
        rowElements.get(5).$("input")
                .shouldHave("true".equals(vip) ? Condition.attribute("checked") : Condition.not(Condition.attribute("checked")));
    }

    /**
     * Просмотр клиента через модальное окно
     */
    public void assertViewClient() {
        List<String> row = getRow(getMainTableRows(), 2);
        String surname = row.get(0);
        String name = row.get(1);
        String patronymic = row.get(2);
        String birthDate = row.get(3);
        String gender = row.get(4);
        String vip = "true".equals(row.get(5)) ? "true" : "false";

        SelenideElement mainPage = getPage();
        getRowElements(mainPage, 2).get(4).click();
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
        getModalDialogBody("Предупреждение").should(Condition.matchText(row.get(0) + " " + row.get(1)));
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
        getModalDialogBody("Предупреждение").should(Condition.matchText(row.get(0) + " " + row.get(1)));
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
     * Проверка master-detail связи
     */
    public void testMasterDetail() {
        getRowElements(getMainTable(), 6).get(0).shouldBe(Condition.text("Чуканова"));
        getRowElements(getMainTable(), 6).get(4).click();

        getContactsList().click();
        getListItemMainContainer(getListItem(getContactsList(), 0)).get(0).shouldBe(Condition.text("3333333"));

        getInput(getCardForm(), "Фамилия").shouldBe(Condition.value("Чуканова"));
        getInput(getCardForm(), "Имя").shouldBe(Condition.value("Изольда"));
        getRadioGroup(getCardForm(), "Пол").$(".checked").shouldBe(Condition.text("Женский"));
        getCheckbox(getCardForm(), "VIP").shouldBe(Condition.enabled);

        getRowElements(getMainTable(), 5).get(4).click();
        getContactsList().click();
        getListItemMainContainer(getListItem(getContactsList(), 0)).get(0).shouldBe(Condition.text("+7950267859"));
        getRadioGroup(getCardForm(), "Пол").$(".checked").shouldBe(Condition.text("Женский"));
        getCheckbox(getCardForm(), "VIP").shouldBe(Condition.enabled);
    }

    /**
     * Проверка создания контакта
     */
    public void assertCreateContact() {
        getMainTableHead().shouldBe(Condition.exist);
        getInput(getMainTableFilter(), "Фамилия").setValue("Маркин");
        getFilterSearchButton().click();
        getRowElements(getMainTable(), 0).get(0).shouldBe(Condition.text("Маркин"));
        getRowElements(getMainTable(), 0).get(4).click();

        SelenideElement contactsList = getContactsList();
        contactsList.click();
        getListItems(contactsList).shouldHave(CollectionCondition.empty);

        getButton(getPanels().get(1), "Создать").click();
        SelenideElement modalPage = getModalPage();
        getInputSelect(modalPage, "Клиент").shouldBe(Condition.exist).click();
        getInputSelect(modalPage, "Клиент").$("input").sendKeys("Маркин");
        getInputSelect(modalPage, "Клиент").$$("button").shouldHaveSize(1);
        getInputSelect(modalPage, "Клиент").$("input").sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        getInputSelect(modalPage, "Тип контакта").click();
        getInputSelect(modalPage, "Тип контакта").$$("button").shouldHaveSize(3);
        getInputSelect(modalPage, "Тип контакта").$("input").sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        getMaskedInput(modalPage, "Номер телефона").click();
        getMaskedInput(modalPage, "Номер телефона").setValue("9999999999");
        getInput(modalPage, "Примечание").setValue("рабочий телефон");
        getButton(modalPage, "Сохранить").click();
        $$(".modal-open").get(0).waitUntil(Condition.not(Condition.exist), 10000);
        getListItems(contactsList).shouldHaveSize(1);
        getListItemMainContainer(getListItem(getContactsList(), 0)).get(0).shouldBe(Condition.text("+7 (999) 999-99-99"));
    }
}
