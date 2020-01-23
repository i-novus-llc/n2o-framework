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

    /////

    default SelenideElement getTableHeaderSurname() {
        return getMainTableHead().$(Selectors.byText("Фамилия"));
    }

    default SelenideElement getFilterSearchButton() {
        return BasePage.getButton(getMainTableFilter(), ("Найти"));
    }

    default SelenideElement getFilterResetButton() {
        return BasePage.getButton(getMainTableFilter(), ("Сбросить"));
    }


    //    default SelenideElement getMainTableHead() {
//        return $("table thead tr");
//    }
//
//    default ElementsCollection getMainTableRows() {
//        return $("table tbody").waitUntil(Condition.exist, 5000).$$("tr");
//    }
//
//    default SelenideElement getMainTableCell(int row, int col) {
//        return getMainTableRows().get(row).$$("td").get(col);
//    }
//
//    default SelenideElement getMainTableFilter() {
//        return $(".n2o-filter");
//    }
//
//    default SelenideElement getAddClientButton() {
//        return $$(".btn-group a").find(Condition.text("Добавить клиента"));
//    }
//
//    default SelenideElement getButtonByLabel(String label) {
//        return $$(".btn-toolbar button").find(Condition.text(label));
//    }
//
//    default SelenideElement getAnchorByLabel(String label) {
//        return $$(".btn-toolbar a").find(Condition.text(label));
//    }
//
//    default SelenideElement getActiveBreadcrumbItem() {
//        return $(".active.breadcrumb-item");
//    }
//
//    default SelenideElement getUpdateButton() {
//        return $$(".btn-group button").find(Condition.text("Изменить"));
//    }
//
//    /////
//
//    default SelenideElement getTableHeaderSurname() {
//        return getMainTableHead().$(Selectors.byText("Фамилия"));
//    }
//
//    default SelenideElement getFilterGenderMale() {
//        return getMainTableFilter().$$(".n2o-checkbox").findBy(Condition.text("Мужской"));
//    }
//
//    default SelenideElement getFilterGenderFemale() {
//        return getMainTableFilter().$$(".n2o-checkbox").findBy(Condition.text("Женский"));
//    }
//
//    default SelenideElement getFilterGenderUnknown() {
//        return getMainTableFilter().$$(".n2o-checkbox").findBy(Condition.text("Не определенный"));
//    }
//
//    default SelenideElement getFilterSearchButton() {
//        return getMainTableFilter().$$("button").findBy(Condition.text("Найти"));
//    }
//
    default SelenideElement getMainTableActivePageNumber(Integer idx) {
        return $$(".pagination li").get(idx);
    }
//
//    default Integer getMainTableActiveRowNumber() {
//        ElementsCollection collection = Selenide.$$("tbody tr");
//        for (int i = 0; i < collection.size(); i++) {
//            if (collection.get(i).getAttribute("class").contains("table-active"))
//                return i;
//        }
//        return -1;
//    }
}
