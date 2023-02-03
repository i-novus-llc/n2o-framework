package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.NumberPicker;
import org.openqa.selenium.Keys;

/**
 * Компонент ввода числа из диапазона
 */
public class N2oNumberPicker extends N2oControl implements NumberPicker {

    @Override
    public void shouldBeEmpty() {
        inputElement().shouldBe(Condition.empty);
    }

    @Override
    public void val(String value) {
        inputElement().click();
        inputElement().setValue(value);
        // focus out
        inputElement().pressTab();
    }

    @Override
    public void clear() {
        inputElement().click();
        inputElement().clear();
        // focus out
        inputElement().pressTab();
    }

    @Override
    public void shouldHaveValue(String value) {
        inputElement().shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
    }

    @Override
    public void clickPlusStepButton() {
        plusButton().click();
    }

    public void minusStepButtonShouldBeEnabled() {
        minusButton().parent().shouldBe(Condition.enabled);
    }

    public void minusStepButtonShouldBeDisabled() {
        minusButton().parent().shouldBe(Condition.disabled);
    }

    @Override
    public void clickMinusStepButton() {
        element().parent().$$(".n2o-number-picker__button .fa-minus").get(0).click();
    }

    public void plusStepButtonShouldBeEnabled() {
        plusButton().parent().shouldBe(Condition.enabled);
    }

    public void plusStepButtonShouldBeDisabled() {
        plusButton().parent().shouldBe(Condition.disabled);
    }

    @Override
    public void minShouldBe(String val) {
        inputElement().shouldBe(Condition.attribute("min", val));
    }

    @Override
    public void maxShouldBe(String val) {
        inputElement().shouldBe(Condition.attribute("max", val));
    }

    @Override
    public void stepShouldBe(String val) {
        inputElement().shouldBe(Condition.attribute("step", val));
    }

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-number-picker__input");
    }

    private SelenideElement minusButton() {
        return element().parent().$(".n2o-number-picker__button .fa-minus");
    }

    private SelenideElement plusButton() {
        return element().parent().$(".n2o-number-picker__button .fa-plus");
    }
}
