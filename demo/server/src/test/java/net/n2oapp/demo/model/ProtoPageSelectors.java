package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

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

    default SelenideElement getMainTablePagination() {
        return $(".n2o-pagination");
    }

    default SelenideElement getTableHeaderSurname() {
        return getMainTableHead().$(Selectors.byText("Фамилия"));
    }

    default SelenideElement getFilterSearchButton() {
        return BasePage.getButton(getMainTableFilter(), ("Найти"));
    }

    default SelenideElement getFilterResetButton() {
        return BasePage.getButton(getMainTableFilter(), ("Сбросить"));
    }

    default SelenideElement getMainTablePaginationButton(int idx) {
        return getMainTablePagination().$$(".page-item").get(idx);
    }

    default SelenideElement getMainTablePaginationActiveButton() {
        return getMainTablePagination().$(".page-item.active");
    }

    default SelenideElement getMainTablePaginationInfo() {
        return $(".n2o-pagination .n2o-pagination-info");
    }
}
