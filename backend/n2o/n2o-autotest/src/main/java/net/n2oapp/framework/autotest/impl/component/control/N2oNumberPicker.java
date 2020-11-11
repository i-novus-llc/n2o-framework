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
        SelenideElement elm = inputElement();
        if (elm.exists()) inputElement().shouldBe(Condition.empty);
        else cellInputElement().shouldBe(Condition.empty);
    }

    @Override
    public String val() {
        SelenideElement elm = inputElement();
        return elm.exists() ? elm.val() : cellInputElement().text();
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
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
        else cellInputElement().shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.text(value));
    }

    @Override
    public void clickPlusStepButton() {
        element().parent().$$(".n2o-number-picker__button .fa-plus").get(0).click();
    }

    @Override
    public void clickMinusStepButton() {
        element().parent().$$(".n2o-number-picker__button .fa-minus").get(1).click();
    }

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-input-number");
    }

    private SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

}
