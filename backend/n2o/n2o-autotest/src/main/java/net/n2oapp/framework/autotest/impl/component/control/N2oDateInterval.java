package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;

import java.time.Duration;

/**
 * Компонент ввода интервала дат для автотестирования
 */
public class N2oDateInterval extends N2oControl implements DateInterval {

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
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
    public void setValueInBegin(String value) {
        firstInputElement().setValue(value);
        element().click();
    }

    @Override
    public void setValueInEnd(String value) {
        lastInputElement().setValue(value);
        element().click();
    }

    @Override
    public void beginShouldHaveValue(String value, Duration... duration) {
        Condition condition = value == null || value.isEmpty() ? Condition.empty : Condition.value(value);
        SelenideElement element = element().$(".n2o-date-input-first input");

        should(condition, element, duration);
    }

    @Override
    public void endShouldHaveValue(String value, Duration... duration) {
        Condition condition = value == null || value.isEmpty() ? Condition.empty : Condition.value(value);
        SelenideElement element = element().$(".n2o-date-input-last input");

        should(condition, element, duration);
    }

    @Override
    public void clickCalendarButton() {
        lastInputElement().parent()
                .$(".btn.n2o-calendar-button")
                .shouldBe(Condition.exist)
                .click();
    }

    @Override
    public void beginDayShouldBeActive(String day) {
        shouldBeActiveDay(firstCalendar(), day);
    }

    @Override
    public void endDayShouldBeActive(String day) {
        shouldBeActiveDay(lastCalendar(), day);
    }

    @Override
    public void beginDayShouldBeDisabled(String day) {
        shouldBeDisableDay(firstCalendar(), day);
    }

    @Override
    public void endDayShouldBeDisabled(String day) {
        shouldBeDisableDay(lastCalendar(), day);
    }

    @Override
    public void beginDayShouldBeEnabled(String day) {
        shouldBeEnableDay(firstCalendar(), day);
    }

    @Override
    public void endDayShouldBeEnabled(String day) {
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
    public void beginCurrentMonthShouldHaveValue(String month, Duration... duration) {
        shouldHaveCurrentMonth(firstCalendar(), month, duration);
    }

    @Override
    public void endCurrentMonthShouldHaveValue(String month, Duration... duration) {
        shouldHaveCurrentMonth(lastCalendar(), month, duration);
    }

    @Override
    public void beginCurrentYearShouldHaveValue(String year, Duration... duration) {
        shouldHaveCurrentYear(firstCalendar(), year, duration);
    }

    @Override
    public void endCurrentYearShouldHaveValue(String year, Duration... duration) {
        shouldHaveCurrentYear(lastCalendar(), year, duration);
    }

    @Override
    public void clickBeginMonthPreviousButton() {
        clickPreviousMonthButton(firstCalendar());
    }

    @Override
    public void clickEndMonthPreviousButton() {
        clickPreviousMonthButton(lastCalendar());
    }

    @Override
    public void clickBeginMonthNextButton() {
        clickNextMonthButton(firstCalendar());
    }

    @Override
    public void clickEndMonthNextButton() {
        clickNextMonthButton(lastCalendar());
    }

    @Override
    public void beginTimeSetValue(String hours, String minutes, String seconds) {
        setTimeValue(firstCalendar(), hours, minutes, seconds);
    }

    @Override
    public void endTimeSetValue(String hours, String minutes, String seconds) {
        setTimeValue(lastCalendar(), hours, minutes, seconds);
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
    public void shouldBeClosed() {
        popUp().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeOpened() {
        popUp().shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeEnabled() {
        firstInputElement().shouldBe(Condition.enabled);
        lastInputElement().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        firstInputElement().shouldBe(Condition.disabled);
        lastInputElement().shouldBe(Condition.disabled);
    }

    @Deprecated
    public void expand() {
        openPopup();
    }

    @Deprecated
    public void collapse() {
        closePopup();
    }

    @Deprecated
    public void shouldBeExpanded() {
        shouldBeOpened();
    }

    @Deprecated
    public void shouldBeCollapsed() {
        shouldBeClosed();
    }

    protected SelenideElement firstInputElement() {
        return element().$(".n2o-date-input-first input");
    }

    protected SelenideElement lastInputElement() {
        return element().$(".n2o-date-input-last input");
    }

    protected SelenideElement firstCalendar() {
        return element().$$(".n2o-calendar").get(0);
    }

    protected SelenideElement lastCalendar() {
        return element().$$(".n2o-calendar").get(1);
    }

    protected SelenideElement popUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }

    private void setTimeValue(SelenideElement element, String hours, String minutes, String seconds) {
        //ToDo: можно ли разделить на отдельный функции?
        element.$(".n2o-calendar-time-container").click();
        element.$$(".n2o-pop-up .hour-picker .n2o-calendar-time-unit").find(Condition.text(hours)).click();
        element.$$(".n2o-pop-up .minute-picker .n2o-calendar-time-unit").find(Condition.text(minutes)).click();
        element.$$(".n2o-pop-up .second-picker .n2o-calendar-time-unit").find(Condition.text(seconds)).click();
        element.$$(".n2o-calendar-time-buttons button").find(Condition.text("Выбрать")).click();
    }

    private void shouldBeActiveDay(SelenideElement element, String day) {
        element.$(".n2o-calendar-day.selected")
                .shouldBe(Condition.exist).shouldHave(Condition.text(day));
    }

    private void shouldBeDisableDay(SelenideElement element, String day) {
        element.$$(".n2o-calendar-day.disabled")
                .find(Condition.text(day))
                .shouldBe(Condition.exist);
    }

    private void shouldBeEnableDay(SelenideElement element, String day) {
        element.$$(".n2o-calendar-day.disabled")
                .find(Condition.text(day))
                .shouldNotBe(Condition.exist);
    }

    private void clickDay(SelenideElement element, String day) {
        element.$$(".n2o-calendar-day").filter(Condition.text(day))
                .exclude(Condition.cssClass("disabled"))
                .exclude(Condition.cssClass("other-month"))
                .get(0).shouldBe(Condition.exist).click();
    }

    private void shouldHaveCurrentMonth(SelenideElement element, String month, Duration... duration) {
        should(Condition.text(month), element.$(".n2o-calendar-header-month-title"), duration);
    }

    private void shouldHaveCurrentYear(SelenideElement element, String year, Duration... duration) {
        should(Condition.text(year), element.$(".n2o-calendar-header-year-title"), duration);
    }

    private void clickPreviousMonthButton(SelenideElement element) {
        element.$(".n2o-calendar-header .fa-angle-left").click();
    }

    private void clickNextMonthButton(SelenideElement element) {
        element.$(".n2o-calendar-header .fa-angle-right").click();
    }
}