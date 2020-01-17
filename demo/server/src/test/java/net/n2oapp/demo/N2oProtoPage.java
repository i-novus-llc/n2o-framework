package net.n2oapp.demo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

/**
 * Селекторы для страницы ProtoPage
 */

import static com.codeborne.selenide.Selenide.*;

public interface N2oProtoPage extends N2oAbstractPage {

    default SelenideElement getMainTableHead() {
        return $("table thead tr");
    }

    default ElementsCollection getMainTableRows() {
        return $("table tbody").waitUntil(Condition.exist, 5000).$$("tr");
    }

    default SelenideElement getMainFilter() {
        return $(".n2o-filter");
    }

    /////

    default void clickSortBySurname() {
        getMainTableHead().$(Selectors.byText("Фамилия")).click();
    }

    default void clickFilterMale() {
        getMainFilter().$$(".n2o-checkbox").findBy(Condition.text("Мужской")).click();
    }

    default void clickFilterFemale() {
        getMainFilter().$$(".n2o-checkbox").findBy(Condition.text("Женский")).click();
    }

    default void clickFilterUnknownGender() {
        getMainFilter().$$(".n2o-checkbox").findBy(Condition.text("Не определенный")).click();
    }

    default void clickSearchFilter() {
        getMainFilter().$$("button").findBy(Condition.text("Найти")).click();
    }
}
