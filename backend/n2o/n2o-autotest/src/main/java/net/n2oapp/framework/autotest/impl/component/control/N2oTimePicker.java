package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.TimePicker;

/**
 * Компонент ввода времени для автотестирования
 */
public class N2oTimePicker extends N2oControl implements TimePicker {

    @Override
    public void shouldBeEmpty() {
        shouldHaveValue("");
    }

    @Override
    public void selectHoursMinutesSeconds(String hours, String minutes, String seconds) {
        openPopup();
        hoursItems().get(Integer.parseInt(hours)).click();
        minutesItems().get(Integer.parseInt(minutes)).click();
        secondsItems().get(Integer.parseInt(seconds)).click();
        closePopup();
    }

    @Override
    public void selectMinutesSeconds(String minutes, String seconds) {
        openPopup();
        minutesItems().get(Integer.parseInt(minutes)).click();
        secondsItems().get(Integer.parseInt(seconds)).click();
        closePopup();
    }

    @Override
    public void selectHours(String hours) {
        openPopup();
        hoursItems().get(Integer.parseInt(hours)).click();
        closePopup();
    }

    @Override
    public void selectMinutes(String minutes) {
        openPopup();
        minutesItems().get(Integer.parseInt(minutes)).click();
        closePopup();
    }

    @Override
    public void shouldHaveValue(String value) {
        element().$(".n2o-input").shouldHave(Condition.attribute("value", value));
    }

    @Override
    public void shouldSelectedHoursMinutesSeconds(String hours, String minutes, String seconds) {
        openPopup();
        hoursActiveItem().shouldHave(Condition.text(hours));
        minutesActiveItem().shouldHave(Condition.text(minutes));
        secondsActiveItem().shouldHave(Condition.text(seconds));
        closePopup();
    }

    @Override
    public void shouldSelectedHoursMinutes(String hours, String minutes) {
        openPopup();
        hoursActiveItem().shouldHave(Condition.text(hours));
        minutesActiveItem().shouldHave(Condition.text(minutes));
        closePopup();
    }

    @Override
    public void shouldSelectedHours(String hours) {
        openPopup();
        hoursActiveItem().shouldHave(Condition.text(hours));
        closePopup();
    }

    @Override
    public void shouldSelectedMinutes(String minutes) {
        openPopup();
        minutesActiveItem().shouldHave(Condition.text(minutes));
        closePopup();
    }

    @Override
    public void shouldHavePrefix(String prefix) {
        prefix().shouldHave(Condition.text(prefix));
    }

    @Override
    public void shouldNotHavePrefix() {
        prefix().shouldNot(Condition.exist);
    }

    @Override
    public void shouldBeOpened() {
        popUp().shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeClosed() {
        popUp().shouldNotBe(Condition.exist);
    }

    @Override
    public void openPopup() {
        if (!popUp().is(Condition.exist))
            element().$(".n2o-input-icon").click();
    }

    @Override
    public void closePopup() {
        if (popUp().is(Condition.exist))
            element().$(".n2o-input-icon").click();
    }

    @Deprecated
    public void shouldBeExpanded() {
        shouldBeOpened();
    }

    @Deprecated
    public void shouldBeCollapsed() {
        shouldBeClosed();
    }

    @Deprecated
    public void expand() {
        openPopup();
    }

    @Deprecated
    public void collapse() {
        closePopup();
    }

    private SelenideElement popUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }

    private SelenideElement prefix() {
        return element().$(".time-prefix");
    }

    private ElementsCollection items(String title) {
        return popUp().$$(".n2o-time-picker__header").findBy(Condition.text(title))
                .parent().$$(".dropdown-item");
    }

    private ElementsCollection hoursItems() {
        return items("часы");
    }

    private ElementsCollection minutesItems() {
        return items("минуты");
    }

    private ElementsCollection secondsItems() {
        return items("секунды");
    }

    private SelenideElement hoursActiveItem() {
        return hoursItems().findBy(Condition.cssClass("active"));
    }

    private SelenideElement minutesActiveItem() {
        return minutesItems().findBy(Condition.cssClass("active"));
    }

    private SelenideElement secondsActiveItem() {
        return secondsItems().findBy(Condition.cssClass("active"));
    }
}
