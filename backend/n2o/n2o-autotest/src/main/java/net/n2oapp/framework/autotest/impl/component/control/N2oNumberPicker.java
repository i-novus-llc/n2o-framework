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
    public void setValue(String value) {
        inputElement().setValue(value);
        // focus out
        inputElement().pressTab();
    }

    public void click() {
        inputElement().click();
    }

    @Override
    public void clear() {
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
    public void shouldHaveMin(String min) {
        inputElement().shouldBe(Condition.attribute("min", min));
    }

    @Override
    public void shouldHaveMax(String max) {
        inputElement().shouldBe(Condition.attribute("max", max));
    }

    @Override
    public void shouldHaveStep(String step) {
        inputElement().shouldBe(Condition.attribute("step", step));
    }

    protected SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-number-picker__input");
    }

    protected SelenideElement minusButton() {
        return element().parent().$(".n2o-number-picker__button .fa-minus");
    }

    protected SelenideElement plusButton() {
        return element().parent().$(".n2o-number-picker__button .fa-plus");
    }
}
