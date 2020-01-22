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

    default SelenideElement getMainTableFilter() {
        return $(".n2o-filter");
    }

    default SelenideElement getAddClientButton() {
        return $$(".btn-group a").find(Condition.text("Добавить клиента"));
    }

    default SelenideElement getActiveBreadcrumbItem() {
        return $(".active.breadcrumb-item");
    }

    default SelenideElement getCreateButton() {
        return $$(".btn-group button").find(Condition.text("Создать"));
    }

    default SelenideElement getUpdateButton() {
        return $$(".btn-toolbar button").find(Condition.text("Изменить"));
    }

    default ElementsCollection getMainTablePaginationButtons() {
        return $(".pagination ").$$("li");
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
