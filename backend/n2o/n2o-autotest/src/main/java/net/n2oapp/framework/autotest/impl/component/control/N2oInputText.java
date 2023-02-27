package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.InputText;

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
    public String getValue() {
        SelenideElement elm = inputElement();
        return elm.exists() ? elm.getValue() : cellInputElement().text();
    }

    @Override
    public void setValue(String value) {
        inputElement().setValue(value);
    }

    @Override
    public void click() {
        inputElement().click();
    }

    @Override
    public void clear() {
        inputElement().clear();
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
        //ToDo: проверить на сходство с методом shouldHaveMeasureText
        inputMeasure().should(Condition.exist);
    }

    @Override
    public void shouldHaveMeasureText(String text) {
        inputMeasure().shouldHave(Condition.text(text));
    }

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-input");
    }

    protected SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    protected SelenideElement inputMeasure() {
        SelenideElement elm = element().parent();
        if (elm.is(Condition.cssClass("n2o-input-number")))
            elm = elm.parent();
        return elm.$(".n2o-control-container-placeholder");
    }
}
