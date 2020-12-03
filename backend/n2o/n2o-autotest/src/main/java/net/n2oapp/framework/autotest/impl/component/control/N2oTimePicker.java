package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.TimePicker;

public class N2oTimePicker extends N2oControl implements TimePicker {

    @Override
    public void shouldBeEmpty() {
        shouldHaveValue("");
    }

    @Override
    public void selectHoursMinutesSeconds(String hours, String minutes, String seconds) {
        expandPopUp();
        hoursItems().get(Integer.parseInt(hours)).click();
        minutesItems().get(Integer.parseInt(minutes)).click();
        secondsItems().get(Integer.parseInt(seconds)).click();
        collapsePopUp();
    }

    @Override
    public void selectMinutesSeconds(String minutes, String seconds) {
        expandPopUp();
        minutesItems().get(Integer.parseInt(minutes)).click();
        secondsItems().get(Integer.parseInt(seconds)).click();
        collapsePopUp();
    }

    @Override
    public void selectHours(String hours) {
        expandPopUp();
        hoursItems().get(Integer.parseInt(hours)).click();
        collapsePopUp();
    }

    @Override
    public void selectMinutes(String minutes) {
        expandPopUp();
        minutesItems().get(Integer.parseInt(minutes)).click();
        collapsePopUp();
    }

    @Override
    public void shouldHaveValue(String value) {
        element().$(".n2o-input").shouldHave(Condition.attribute("value", value));
    }

    @Override
    public void shouldSelectedHoursMinutesSeconds(String hours, String minutes, String seconds) {
        expandPopUp();
        hoursActiveItem().shouldHave(Condition.text(hours));
        minutesActiveItem().shouldHave(Condition.text(minutes));
        secondsActiveItem().shouldHave(Condition.text(seconds));
        collapsePopUp();
    }

    @Override
    public void shouldSelectedHoursMinutes(String hours, String minutes) {
        expandPopUp();
        hoursActiveItem().shouldHave(Condition.text(hours));
        minutesActiveItem().shouldHave(Condition.text(minutes));
        collapsePopUp();
    }

    @Override
    public void shouldSelectedHours(String hours) {
        expandPopUp();
        hoursActiveItem().shouldHave(Condition.text(hours));
        collapsePopUp();
    }

    @Override
    public void shouldSelectedMinutes(String minutes) {
        expandPopUp();
        minutesActiveItem().shouldHave(Condition.text(minutes));
        collapsePopUp();
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
    public void shouldBeExpanded() {
        popUp().shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeCollapsed() {
        popUp().shouldNotBe(Condition.exist);
    }

    @Override
    public void expandPopUp() {
        if (!popUp().is(Condition.exist))
            element().$(".n2o-input-icon").click();
    }

    @Override
    public void collapsePopUp() {
        if (popUp().is(Condition.exist))
            element().$(".n2o-input-icon").click();
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
