package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import org.openqa.selenium.Keys;

/**
 * Компонент ввода интервала дат для автотестирования
 */
public class N2oDateInterval extends N2oControl implements DateInterval {

    @Override
    public void shouldHaveValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldBeEmpty() {
        beginShouldBeEmpty();
        endShouldBeEmpty();
    }

    @Override
    public void beginShouldBeEmpty() {
        firstInputElement().shouldBe(Condition.empty);
    }

    @Override
    public void endShouldBeEmpty() {
        lastInputElement().shouldBe(Condition.empty);
    }

    @Override
    public void beginVal(String value) {
        firstInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        element().click();
    }

    @Override
    public void endVal(String value) {
        lastInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        element().click();
    }

    @Override
    public void beginShouldHaveValue(String value) {
        element().$(".n2o-date-input-first input").shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
    }

    @Override
    public void endShouldHaveValue(String value) {
        element().$(".n2o-date-input-last input").shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
    }

    @Override
    public void clickCalendarButton() {
        lastInputElement().parent().$(".btn.n2o-calendar-button").shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldBeBeginActiveDay(String day) {
        shouldBeActiveDay(firstCalendar(), day);
    }

    @Override
    public void shouldBeEndActiveDay(String day) {
        shouldBeActiveDay(lastCalendar(), day);
    }

    @Override
    public void shouldBeDisableBeginDay(String day) {
        shouldBeDisableDay(firstCalendar(), day);
    }

    @Override
    public void shouldBeDisableEndDay(String day) {
        shouldBeDisableDay(lastCalendar(), day);
    }

    @Override
    public void shouldBeEnableBeginDay(String day) {
        shouldBeEnableDay(firstCalendar(), day);
    }

    @Override
    public void shouldBeEnableEndDay(String day) {
        shouldBeEnableDay(lastCalendar(), day);
    }

    @Override
    public void clickBeginDay(String day) {
        clickDay(firstCalendar(), day);
    }

    @Override
    public void clickEndDay(String day) {
        clickDay(lastCalendar(), day);
    }

    @Override
    public void beginShouldHaveCurrentMonth(String month) {
        shouldHaveCurrentMonth(firstCalendar(), month);
    }

    @Override
    public void endShouldHaveCurrentMonth(String month) {
        shouldHaveCurrentMonth(lastCalendar(), month);
    }

    @Override
    public void beginShouldHaveCurrentYear(String year) {
        shouldHaveCurrentYear(firstCalendar(), year);
    }

    @Override
    public void endShouldHaveCurrentYear(String year) {
        shouldHaveCurrentYear(lastCalendar(), year);
    }

    @Override
    public void clickBeginPreviousMonthButton() {
        clickPreviousMonthButton(firstCalendar());
    }

    @Override
    public void clickEndPreviousMonthButton() {
        clickPreviousMonthButton(lastCalendar());
    }

    @Override
    public void clickBeginNextMonthButton() {
        clickNextMonthButton(firstCalendar());
    }

    @Override
    public void clickEndNextMonthButton() {
        clickNextMonthButton(lastCalendar());
    }

    @Override
    public void beginTimeVal(String hours, String minutes, String seconds) {
        timeVal(firstCalendar(), hours, minutes, seconds);
    }

    @Override
    public void endTimeVal(String hours, String minutes, String seconds) {
        timeVal(lastCalendar(), hours, minutes, seconds);
    }

    @Override
    public void expand() {
        element().$(".n2o-date-input").should(Condition.exist).click();
    }

    @Override
    public void collapse() {
        throw new UnsupportedOperationException("Date pop-up cannot be closed without choosing the date");
    }

    @Override
    public void shouldBeCollapsed() {
        popUp().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeExpanded() {
        popUp().shouldBe(Condition.exist);
    }

    private void timeVal(SelenideElement element, String hours, String minutes, String seconds) {
        element.$(".n2o-calendar-time-container").click();
        element.$$(".n2o-pop-up .hour-picker .n2o-calendar-time-unit").find(Condition.text(hours)).click();
        element.$$(".n2o-pop-up .minute-picker .n2o-calendar-time-unit").find(Condition.text(minutes)).click();
        element.$$(".n2o-pop-up .second-picker .n2o-calendar-time-unit").find(Condition.text(seconds)).click();
        element.$$(".n2o-calendar-time-buttons button").find(Condition.text("Выбрать")).click();
    }

    private SelenideElement firstInputElement() {
        return element().$(".n2o-date-input-first input");
    }

    private SelenideElement lastInputElement() {
        return element().$(".n2o-date-input-last input");
    }

    private SelenideElement firstCalendar() {
        return element().$$(".n2o-calendar").get(0);
    }

    private SelenideElement lastCalendar() {
        return element().$$(".n2o-calendar").get(1);
    }

    private void shouldBeActiveDay(SelenideElement element, String day) {
        element.$(".n2o-calendar-day.selected").shouldBe(Condition.exist).shouldHave(Condition.text(day));
    }

    private void shouldBeDisableDay(SelenideElement element, String day) {
        element.$$(".n2o-calendar-day.disabled").find(Condition.text(day)).shouldBe(Condition.exist);
    }

    private void shouldBeEnableDay(SelenideElement element, String day) {
        element.$$(".n2o-calendar-day.disabled").find(Condition.text(day)).shouldNotBe(Condition.exist);
    }

    private void clickDay(SelenideElement element, String day) {
        element.$$(".n2o-calendar-day").filter(Condition.text(day))
                .exclude(Condition.cssClass("disabled"))
                .exclude(Condition.cssClass("other-month"))
                .get(0).shouldBe(Condition.exist).click();
    }

    private void shouldHaveCurrentMonth(SelenideElement element, String month) {
        element.$(".n2o-calendar-header-month-title").shouldHave(Condition.text(month));
    }

    private void shouldHaveCurrentYear(SelenideElement element, String year) {
        element.$(".n2o-calendar-header-year-title").shouldHave(Condition.text(year));
    }

    private void clickPreviousMonthButton(SelenideElement element) {
        element.$(".n2o-calendar-header .fa-angle-left").click();
    }

    private void clickNextMonthButton(SelenideElement element) {
        element.$(".n2o-calendar-header .fa-angle-right").click();
    }

    private SelenideElement popUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }
}