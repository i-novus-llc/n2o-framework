package net.n2oapp.demo;

import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

/**
 * Главная страница демо приложения
 */
public class ProtoPage {

    public ProtoPage findBySurname(String query) {
        $$(".n2o-filter input").get(0).val(query);
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage tableShouldHaveSize(int size) {
        $$("tbody tr").shouldHaveSize(size);
        return page(ProtoPage.class);
    }

    public ProtoPage assertSurname(Integer rowIndex, String surname) {
        $$("tbody tr").get(rowIndex).$("button").shouldHave(text(surname));
        return page(ProtoPage.class);
    }

    public ProtoPage assertCurrentPageNumber(int pageNumber) {
        $(".n2o-pagination .active").shouldHave(text(String.valueOf(pageNumber)));
        return page(ProtoPage.class);
    }

    public ProtoPage tableShouldHavePage(int pageNumber) {
        $$(".n2o-pagination li").get(pageNumber - 1).exists();
        return page(ProtoPage.class);
    }
}
