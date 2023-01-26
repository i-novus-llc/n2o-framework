package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import org.openqa.selenium.Keys;

/**
 * Компонент ввода даты для автотестирования
 */
public class N2oDateInput extends N2oControl implements DateInput {

    @Override
    public void shouldBeEmpty() {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldBe(Condition.empty);
        else cellInputElement().shouldBe(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value) {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(value == null || value.isEmpty() ? Condition.empty : Condition.value(value));
        else cellInputElement().shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.text(value));
    }

    @Override
    public String val() {
        SelenideElement elm = inputElement();
        return elm.exists() ? elm.val() : cellInputElement().text();
    }

    @Override
    public void val(String value) {
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
        element().$(".n2o-calendar-time-container").click();
        element().$$(".n2o-pop-up .hour-picker .n2o-calendar-time-unit").find(Condition.text(hours)).click();
        element().$$(".n2o-pop-up .minute-picker .n2o-calendar-time-unit").find(Condition.text(minutes)).click();
        element().$$(".n2o-pop-up .second-picker .n2o-calendar-time-unit").find(Condition.text(seconds)).click();
        element().$$(".n2o-calendar-time-buttons button").find(Condition.text("Выбрать")).click();
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
        element().$(".n2o-calendar-day.selected").shouldBe(Condition.exist).shouldHave(Condition.text(day));
    }

    @Override
    public void clickDay(String day) {
        element().$$(".n2o-calendar-day").filter(Condition.text(day))
                .exclude(Condition.cssClass("disabled"))
                .exclude(Condition.cssClass("other-month"))
                .get(0).shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldBeDisableDay(String day) {
        element().$$(".n2o-calendar-day.disabled").find(Condition.text(day)).shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeDisableDay(String day) {
        element().$$(".n2o-calendar-day.disabled").find(Condition.text(day)).shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveCurrentMonth(String month) {
        element().$(".n2o-calendar-header-month-title").shouldHave(Condition.text(month));
    }

    @Override
    public void shouldHaveCurrentYear(String year) {
        element().$(".n2o-calendar-header-year-title").shouldHave(Condition.text(year));
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
        Condition condition = Condition.attribute("placeholder", value);

        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(condition);
        else cellInputElement().shouldHave(condition);
    }

    @Deprecated
    public void expand() {
        openPopup();
    }

    @Override
    public void openPopup() {
        element().$(".n2o-date-input").should(Condition.exist).click();
    }

    @Deprecated
    public void collapse() {
        closePopup();
    }

    @Override
    public void closePopup() {
        throw new UnsupportedOperationException("Date pop-up cannot be closed without choosing the date");
    }

    @Deprecated
    public void shouldBeExpanded() {
        shouldBeOpened();
    }

    @Override
    public void shouldBeOpened() {
        popUp().shouldBe(Condition.exist);
    }

    @Deprecated
    public void shouldBeCollapsed() {
        shouldBeClosed();
    }

    @Override
    public void shouldBeClosed() {
        popUp().shouldNotBe(Condition.exist);
    }

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().$(".n2o-date-input input");
    }

    private SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    private SelenideElement popUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }
}
