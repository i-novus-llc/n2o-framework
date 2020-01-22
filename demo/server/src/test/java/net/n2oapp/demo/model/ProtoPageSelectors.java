package net.n2oapp.demo.model;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Селекторы для страницы ProtoPage
 */

public interface ProtoPageSelectors extends BasePage {

    default SelenideElement getMainTableHead() {
        return $("table thead tr");
    }

    default ElementsCollection getMainTableRows() {
        return $("table tbody").waitUntil(Condition.exist, 5000).$$("tr");
    }

    default SelenideElement getMainTableCell(int row, int col) {
        return getMainTableRows().get(row).$$("td").get(col);
    }

    default SelenideElement getMainTableFilter() {
        return $(".n2o-filter");
    }

    default SelenideElement getAddClientButton() {
        return $$(".btn-group a").find(Condition.text("Добавить клиента"));
    }

    default SelenideElement getButtonByLabel(String label) {
        return $$(".btn-toolbar button").find(Condition.text(label));
    }

    default SelenideElement getAnchorByLabel(String label) {
        return $$(".btn-toolbar a").find(Condition.text(label));
    }

    default SelenideElement getActiveBreadcrumbItem() {
        return $(".active.breadcrumb-item");
    }

    default SelenideElement getUpdateButton() {
        return $$(".btn-group button").find(Condition.text("Изменить"));
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

    default Integer getMainTableActivePageNumber() {
        return Integer.valueOf(Selenide.$(".pagination li.active").text());
    }

    default Integer getMainTableActiveRowNumber() {
        ElementsCollection collection = Selenide.$$("tbody tr");
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getAttribute("class").contains("table-active"))
                return i;
        }
        return -1;
    }
}
