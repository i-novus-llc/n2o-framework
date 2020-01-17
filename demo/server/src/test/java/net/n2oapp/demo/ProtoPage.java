package net.n2oapp.demo;

import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.page;

/**
 * Главная страница демо приложения
 */
public class ProtoPage {

    public ProtoPage tableShouldHaveSize(int size) {
        $$("tbody tr").shouldHaveSize(size);
        return page(ProtoPage.class);
    }

    public ProtoPage findByName(String name) {
        $$(".n2o-filter input").get(1).val(name);
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage findBySurname(String surname) {
        $$(".n2o-filter input").get(0).val(surname);
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage assertName(Integer rowIndex, String name) {
        $$("tbody tr").get(rowIndex).$$("button").get(1).shouldHave(text(name));
        return page(ProtoPage.class);
    }

    public ProtoPage assertSurname(Integer rowIndex, String surname) {
        $$("tbody tr").get(rowIndex).$("button").shouldHave(text(surname));
        return page(ProtoPage.class);
    }
}
