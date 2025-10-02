package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;

import java.time.Duration;

/**
 * Реализация поля {@code <masked-input>} для автотестов
 */
public class N2oMaskedInput extends N2oMaskedControl implements MaskedInput {

    @Override
    public String getValue() {
        SelenideElement input = inputElement();

        return input.exists() ? input.getValue() : cellInputElement().text();
    }

    @Override
    public void shouldHaveMeasure() {
        inputMeasure().should(Condition.exist);
    }

    @Override
    public void shouldHaveMeasureText(String text) {
        inputMeasure().shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveInvalidText(WebElementCondition condition, Duration... duration) {
        should(condition, element().parent().parent().parent().$(".n2o-validation-message"), duration);
    }
}