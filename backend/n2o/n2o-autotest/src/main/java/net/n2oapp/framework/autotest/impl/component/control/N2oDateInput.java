package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import org.openqa.selenium.Keys;

import java.time.Duration;

/**
 * Компонент ввода даты для автотестирования
 */
public class N2oDateInput extends N2oControl implements DateInput {

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();
        if (input.exists())
            input.shouldBe(Condition.empty);
        else
            cellInputElement().shouldBe(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        SelenideElement input = inputElement();

        if (input.exists())
            should(Condition.value(value), input, duration);
        else
            should(Condition.text(value), cellInputElement(), duration);
    }

    @Override
    public String getValue() {
        SelenideElement input = inputElement();
        return input.exists() ? input.getValue() : cellInputElement().text();
    }

    @Override
    public void setValue(String value) {
        boolean isEditableCell = element().is(Condition.cssClass("n2o-editable-cell"));

        if (!inputElement().exists() && isEditableCell)
            element().click();
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        if (isEditableCell)
            inputElement().sendKeys(Keys.chord(Keys.ENTER));
        else
            inputElement().click();
    }

    @Override
    public void timeVal(String hours, String minutes, String seconds) {
        //ToDo: Можно ли разделить это по отдельным функциям? Целесообразно ли?
        element().$(".n2o-calendar-time-container").click();
        element().$$(".n2o-pop-up .hour-picker .n2o-calendar-time-unit")
                .find(Condition.text(hours)).click();
        element().$$(".n2o-pop-up .minute-picker .n2o-calendar-time-unit")
                .find(Condition.text(minutes)).click();
        element().$$(".n2o-pop-up .second-picker .n2o-calendar-time-unit")
                .find(Condition.text(seconds)).click();
        element().$$(".n2o-calendar-time-buttons button")
                .find(Condition.text("Выбрать")).click();
    }

    @Override
    public void shouldBeDisabled() {
        inputElement().shouldBe(Condition.disabled);
    }

    @Override
    public void clickCalendarButton() {
        element().$(".btn.n2o-calendar-button").click();
    }

    @Override
    public void shouldBeActiveDay(String day) {
        calendarDays().findBy(Condition.cssClass("selected"))
                .shouldBe(Condition.exist)
                .shouldHave(Condition.text(day));
    }

    @Override
    public void clickDay(String day) {
        calendarDays().filter(Condition.text(day))
                .exclude(Condition.cssClass("disabled"))
                .exclude(Condition.cssClass("other-month"))
                .get(0).shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldBeDisableDay(String day) {
        calendarDays().filterBy(Condition.cssClass("disabled"))
                .find(Condition.text(day))
                .shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeDisableDay(String day) {
        calendarDays().filterBy(Condition.cssClass("disabled"))
                .find(Condition.text(day))
                .shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveCurrentMonth(String month, Duration... duration) {
        should(Condition.text(month), element().$(".n2o-calendar-header-month-title"), duration);
    }

    @Override
    public void shouldHaveCurrentYear(String year, Duration... duration) {
        should(Condition.text(year), element().$(".n2o-calendar-header-year-title"), duration);
    }

    @Override
    public void clickPreviousMonthButton() {
        element().$(".n2o-calendar-header .fa-angle-left").click();
    }

    @Override
    public void clickNextMonthButton() {
        element().$(".n2o-calendar-header .fa-angle-right").click();
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        WebElementCondition condition = Condition.attribute("placeholder", value);

        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(condition);
        else cellInputElement().shouldHave(condition);
    }

    @Override
    public void openPopup() {
        element().$(".n2o-date-input").should(Condition.exist).click();
    }

    @Override
    public void closePopup() {
        throw new UnsupportedOperationException("Date pop-up cannot be closed without choosing the date");
    }

    @Override
    public void shouldBeOpened() {
        popUp().shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeClosed() {
        popUp().shouldNotBe(Condition.exist);
    }

    protected SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().$(".n2o-date-input input");
    }

    protected SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    protected SelenideElement popUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }

    protected ElementsCollection calendarDays() {
        return element().$$(".n2o-calendar-day");
    }
}
