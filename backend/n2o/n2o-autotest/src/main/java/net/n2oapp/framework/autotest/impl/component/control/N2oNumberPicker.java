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
    public String val() {
        return inputElement().val();
    }

    @Override
    public void val(String value) {
        inputElement().click();
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    @Override
    public void clear() {
        inputElement().click();
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }

    @Override
    public void shouldHaveValue(String value) {
        inputElement().shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
    }

    @Override
    public void clickPlusStepButton() {
        element().parent().$$(".n2o-number-picker__button .fa-plus").get(0).click();
    }

    @Override
    public void clickMinusStepButton() {
        element().parent().$$(".n2o-number-picker__button .fa-minus").get(0).click();
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

}
