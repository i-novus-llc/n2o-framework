package net.n2oapp.demo;

import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

/**
 * Главная страница демо приложения
 */
public class ProtoPage {

    public ProtoPage findBySurname(String query) {
        $(".n2o-filter").$$("input").get(0).val(query);
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage tableShouldHaveSize(int size) {
        $$("tbody").shouldHaveSize(size);
        return page(ProtoPage.class);
    }

    public ProtoPage assertSurname(Integer rowIndex, String surname) {
        $$("tbody").get(rowIndex).$("button").shouldHave(text(surname));
        return page(ProtoPage.class);
    }
}
