package net.n2oapp.demo;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.openqa.selenium.Keys;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static net.n2oapp.demo.BasePage.*;
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
     * Проверка работы фильтра по дате рождения
     */
    public void testFilterByBirthday() {
        getDateIntervalStart(getMainTableFilter(), "Дата рождения").val("01.01.1940");
        getDateIntervalEnd(getMainTableFilter(), "Дата рождения").val("01.12.1940");
        getFilterSearchButton().click();

        getMainTableRows().shouldHaveSize(2);
        getRowElements(getMainTable(), 0).get(0).shouldHave(text("Кручинина"));
        getRowElements(getMainTable(), 1).get(0).shouldHave(text("Мишин"));
    }
}
