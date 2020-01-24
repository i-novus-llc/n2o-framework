package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Селекторы для страницы ProtoPage
 */
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
        return getMainTableHead().$(byText("Фамилия"));
    }

    default SelenideElement getFilterSearchButton() {
        return BasePage.getButton(getMainTableFilter(), ("Найти"));
    }

    default SelenideElement getFilterResetButton() {
        return BasePage.getButton(getMainTableFilter(), ("Сбросить"));
    }

    default SelenideElement getMainTablePaginationButton(Integer idx) {
        return $$(".pagination li").get(idx);
    }

    default SelenideElement getContactsList() {
        return $$(".n2o-panel-region").get(1).$$(".n2o-widget-list").get(0);
    }

    default SelenideElement getCardForm() {
        return $$(".n2o-panel-region").get(2).$$(".card-body").get(0);
    }

    default ElementsCollection getPanels() {
        return $$(".n2o-panel-region");
    }
}
