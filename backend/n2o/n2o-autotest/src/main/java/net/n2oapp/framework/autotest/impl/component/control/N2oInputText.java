package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.InputText;

/**
 * Компонент ввода текста для автотестирования
 */
public class N2oInputText extends N2oControl implements InputText {

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();
        if (input.exists()) input.shouldBe(Condition.empty);
        else cellInputElement().shouldBe(Condition.empty);
    }

    @Override
    public String getValue() {
        SelenideElement input = inputElement();
        return input.exists() ? input.getValue() : cellInputElement().text();
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
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(Condition.exactValue(value));
        else
            cellInputElement().shouldHave(Condition.selectedText(value));
    }

    @Override
    public void shouldHavePlaceholder(String placeholder) {
        Condition condition = Condition.attribute("placeholder", placeholder);
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(condition);
        else
            cellInputElement().shouldHave(condition);
    }

    @Override
    public void clickPlusStepButton() {
        stepButton().get(0).click();
    }

    @Override
    public void clickMinusStepButton() {
        stepButton().get(1).click();
    }

    @Override
    public void shouldHaveMeasure() {
        inputMeasure().should(Condition.exist);
    }

    @Override
    public void shouldHaveMeasureText(String text) {
        inputMeasure().shouldHave(Condition.text(text));
    }

    protected ElementsCollection stepButton() {
        return element().parent().$$(".n2o-input-number-buttons button");
    }

    protected SelenideElement inputElement() {
        return element().shouldBe(Condition.exist).parent().$(".n2o-input");
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
