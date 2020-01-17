package net.n2oapp.demo;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

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
        $$("tbody tr").get(rowIndex).$("button").shouldHave(text(surname));
        return page(ProtoPage.class);
    }

    public ProtoPage clearFilters() {
        $(".n2o-filter").$(Selectors.byText("Сбросить")).click();
        return page(ProtoPage.class);
    }

    public ProtoPage assertClearFilterFields() {
        ElementsCollection fields = $$(".n2o-filter input");
        fields.get(0).shouldHave(value(""));
        fields.get(1).shouldHave(value(""));
        fields.get(2).shouldHave(value(""));
        fields.get(3).shouldHave(value(""));
        fields.get(4).shouldNotHave(checked);
        fields.get(5).shouldNotHave(checked);
        fields.get(6).shouldNotHave(checked);
        fields.get(7).shouldNotHave(checked);
        return page(ProtoPage.class);
    }
}
