package net.n2oapp.demo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

/**
 * Селекторы для страницы ProtoPage
 */

import static com.codeborne.selenide.Selenide.*;

public interface ProtoPageSelectors {

    default SelenideElement getMainTable() {
        return $("table");
    }

    default SelenideElement getMainTableHead() {
        return getMainTable().$("thead tr");
    }

    default ElementsCollection getMainTableRows() {
        return getMainTable().$("tbody").shouldBe(Condition.exist).$$("tr");
    }

    default SelenideElement getMainTableFilter() {
        return $(".n2o-filter");
    }

    /////

    default SelenideElement getTableHeaderSurname() {
        return getMainTableHead().$(Selectors.byText("Фамилия"));
    }

    default SelenideElement getFilterSearchButton() {
        return BasePage.getButton(getMainTableFilter(),("Найти"));
    }

    default SelenideElement getFilterResetButton() {
        return BasePage.getButton(getMainTableFilter(),("Сбросить"));
    }
}
