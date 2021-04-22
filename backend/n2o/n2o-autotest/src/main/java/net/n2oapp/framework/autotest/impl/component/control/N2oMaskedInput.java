package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;
import org.openqa.selenium.Keys;

/**
 * Компонент ввода текста с маской для автотестирования
 */
public class N2oMaskedInput extends N2oControl implements MaskedInput {

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
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        inputElement().pressEnter();
    }

    @Override
    public void shouldHaveValue(String value) {
        SelenideElement elm = element().parent().$(".n2o-input-mask");
        if (elm.exists()) elm.shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.text(value));
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        Condition condition = Condition.attribute("placeholder", value);

        SelenideElement elm = element().parent().$(".n2o-input-mask");
        if (elm.exists()) elm.shouldHave(condition);
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(condition);
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
        return element().parent().$(".n2o-input-mask");
    }

    private SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    private SelenideElement inputMeasure() {
        return element().parent().$(".n2o-control-container-placeholder");
    }
}
