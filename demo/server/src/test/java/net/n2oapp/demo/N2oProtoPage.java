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

    default SelenideElement getMainTableFilter() {
        return $(".n2o-filter");
    }

    /////

    default SelenideElement getTableHeaderSurname() {
        return getMainTableHead().$(Selectors.byText("Фамилия"));
    }

    default SelenideElement getFilterGenderMale() {
        return getMainTableFilter().$$(".n2o-checkbox").findBy(Condition.text("Мужской"));
    }

    default SelenideElement getFilterGenderFemale() {
        return getMainTableFilter().$$(".n2o-checkbox").findBy(Condition.text("Женский"));
    }

    default SelenideElement getFilterGenderUnknown() {
        return getMainTableFilter().$$(".n2o-checkbox").findBy(Condition.text("Не определенный"));
    }

    default SelenideElement getFilterSearchButton() {
        return getMainTableFilter().$$("button").findBy(Condition.text("Найти"));
    }
}
