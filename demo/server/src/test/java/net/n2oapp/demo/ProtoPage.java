package net.n2oapp.demo;

import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

/**
 * Главная страница демо приложения
 */
public class ProtoPage {

    public ProtoPage tableShouldHaveSize(int size) {
        $("tbody").$$("tr").shouldHaveSize(size);
        return page(ProtoPage.class);
    }

    public ProtoPage findByName(String name) {
        $(".n2o-filter").$$("input").get(1).val(name);
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage findBySurname(String surname) {
        $(".n2o-filter").$$("input").get(0).val(surname);
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage findByGender(String gender) {
        $(".n2o-filter").$(Selectors.byText(gender)).click();
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage findByVip() {
        $(".n2o-filter").$(Selectors.byText("VIP")).click();
        $(".n2o-filter").$(Selectors.byText("Найти")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage assertSurname(Integer rowIndex, String surname) {
        $("tbody").$$("tr").get(rowIndex).$("button").shouldHave(text(surname));
        return page(ProtoPage.class);
    }

    public ProtoPage clearFilters() {
        $(".n2o-filter").$(Selectors.byText("Сбросить")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage assertClearFilterFields() {
        $(".n2o-filter").$$("input").get(0).getValue();
        $(".n2o-filter").$$("input").get(1).getValue();
        $(".n2o-filter").$$("input").get(2).getValue();
        $(".n2o-filter").$$("input").get(3).getValue();
        $(".n2o-filter").$$("input").get(4).isSelected();
        $(".n2o-filter").$$("input").get(5).shouldHave(null);
        $(".n2o-filter").$$("input").get(6).shouldHave(null);
        $(".n2o-filter").$$("input").get(7).shouldHave(null);
        return page(ProtoPage.class);
    }
}
