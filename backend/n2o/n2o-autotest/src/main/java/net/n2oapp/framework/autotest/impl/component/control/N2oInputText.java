package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import org.openqa.selenium.Keys;

/**
 * Компонент ввода текста для автотестирования
 */
public class N2oInputText extends N2oControl implements InputText {

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
        clear();
        inputElement().sendKeys(value);
    }

    @Override
    public void clear() {
        inputElement().sendKeys(Keys.CONTROL + "a");
        inputElement().sendKeys(Keys.BACK_SPACE);
    }

    @Override
    public void shouldHaveValue(String value) {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.exactValue(value));
        else cellInputElement().shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.selectedText(value));
    }

    @Override
    public void shouldHavePlaceholder(String placeholder) {
        Condition condition = Condition.attribute("placeholder", placeholder);

        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(condition);
        else cellInputElement().shouldHave(condition);
    }

    @Override
    public void clickPlusStepButton() {
        element().parent().$$(".n2o-input-number-buttons button").get(0).click();
    }

    @Override
    public void clickMinusStepButton() {
        element().parent().$$(".n2o-input-number-buttons button").get(1).click();
    }

    @Override
    public void shouldHaveMeasure() {
        inputMeasure().should(Condition.exist);
    }

    @Override
    public void measureShouldHaveText(String text) {
        inputMeasure().shouldHave(Condition.text(text));
    }

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-input");
    }

    private SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    private SelenideElement inputMeasure() {
        SelenideElement elm = element().parent();
        if (elm.is(Condition.cssClass("n2o-input-number")))
            elm = elm.parent();
        return elm.$(".n2o-control-container-placeholder");
    }
}
