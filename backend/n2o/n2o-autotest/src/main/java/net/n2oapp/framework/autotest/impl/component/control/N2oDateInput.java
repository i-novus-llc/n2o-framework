package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import org.openqa.selenium.Keys;

/**
 * Поле ввода даты (date-time) для автотестирования
 */
public class N2oDateInput extends N2oControl implements DateInput {

    @Override
    public void shouldHaveValue(String value) {
        SelenideElement elm = element().$(".n2o-date-input input");
        if (elm.exists()) elm.shouldHave(Condition.value(value));
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(Condition.text(value));
    }

    @Override
    public String val() {
        SelenideElement elm = element().$(".n2o-date-input input");
        return elm.exists() ? elm.val() : element().$(".n2o-editable-cell .n2o-editable-cell-text").text();
    }

    @Override
    public void val(String value) {
        element().$(".n2o-date-input input").sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        element().$(".n2o-date-input input").pressEnter();
    }

    @Override
    public void shouldBeDisabled() {
        element().$(".n2o-date-input input").shouldBe(Condition.disabled);
    }

    @Override
    public void clickCalendarButton() {
        element().$(".n2o-calendar-button").click();
    }

    @Override
    public void shouldBeActiveDay(String day) {
        element().$(".n2o-calendar-day.selected").shouldHave(Condition.text(day));
    }
}
