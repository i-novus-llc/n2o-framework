package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;

import java.time.Duration;

/**
 * Компонент ввода текста с маской для автотестирования
 */
public class N2oMaskedInput extends N2oControl implements MaskedInput {

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();

        if (input.exists())
            inputElement().shouldBe(Condition.empty);
        else
            cellInputElement().shouldBe(Condition.empty);
    }

    @Override
    public String getValue() {
        SelenideElement input = inputElement();

        return input.exists() ? input.getValue() : cellInputElement().text();
    }

    @Override
    public void setValue(String value) {
        inputElement().setValue(value).pressEnter();
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        SelenideElement input = inputElement();

        if (input.exists())
            should(Condition.value(value), input, duration);
        else
            should(Condition.text(value), cellInputElement(), duration);
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        WebElementCondition condition = Condition.attribute("placeholder", value);
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(condition);
        else
            cellInputElement().shouldHave(condition);
    }

    @Override
    public void shouldHaveMeasure() {
        inputMeasure().should(Condition.exist);
    }

    @Override
    public void shouldHaveMeasureText(String text) {
        inputMeasure().shouldHave(Condition.text(text));
    }

    protected SelenideElement inputElement() {
        return element().shouldBe(Condition.exist).parent().$(".n2o-input-mask");
    }

    protected SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    protected SelenideElement inputMeasure() {
        return element().parent().$(".n2o-control-container-placeholder");
    }
}
