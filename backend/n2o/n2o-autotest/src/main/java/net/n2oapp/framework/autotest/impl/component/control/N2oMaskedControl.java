package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.MaskedControl;

import java.time.Duration;

/**
 * Реализация поля ввода с маской для автотестов
 */
public class N2oMaskedControl extends N2oControl implements MaskedControl {

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();

        if (input.exists())
            inputElement().shouldBe(Condition.empty);
        else
            cellInputElement().shouldBe(Condition.empty);
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
    public void setValue(String value) {
        inputElement().setValue(value).pressEnter();
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
    public void shouldHaveInvalidText(WebElementCondition condition, Duration... duration) {
        should(condition, element().parent().parent().$(".n2o-validation-message"), duration);
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
