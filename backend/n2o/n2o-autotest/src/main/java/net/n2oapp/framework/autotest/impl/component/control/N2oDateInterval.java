package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;

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
        //ToDo зачем такой метод, если есь две public проверки
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
        //ToDo разделить в два разных метода
        firstInputElement().setValue(value);
        element().click();
    }

    @Override
    public void setValueInEnd(String value) {
        //ToDo разделить в два разных метода
        lastInputElement().setValue(value);
        element().click();
    }

    @Override
    public void beginShouldHaveValue(String value) {
        element().$(".n2o-date-input-first input").shouldHave(value == null
                || value.isEmpty() ? Condition.empty : Condition.value(value));
    }

    @Override
    public void endShouldHaveValue(String value) {
        element().$(".n2o-date-input-last input").shouldHave(value == null
                || value.isEmpty() ? Condition.empty : Condition.value(value));
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
    public void beginCurrentMonthShouldHaveValue(String month) {
        shouldHaveCurrentMonth(firstCalendar(), month);
    }

    @Override
    public void endCurrentMonthShouldHaveValue(String month) {
        shouldHaveCurrentMonth(lastCalendar(), month);
    }

    @Override
    public void beginCurrentYearShouldHaveValue(String year) {
        shouldHaveCurrentYear(firstCalendar(), year);
    }

    @Override
    public void endCurrentYearShouldHaveValue(String year) {
        shouldHaveCurrentYear(lastCalendar(), year);
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
        //ToDo разделить в два разных метода, так как проверка содержащая две проверки ухудшает читабельность отчета при ошибке
        firstInputElement().shouldBe(Condition.enabled);
        lastInputElement().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        //ToDo разделить в два разных метода, так как проверка содержащая две проверки ухудшает читабельность отчета при ошибке
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
}