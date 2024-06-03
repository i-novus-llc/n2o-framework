package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.TextArea;

import java.time.Duration;

/**
 * Поле ввода многострочного текста для автотестирования
 */
public class N2oTextArea extends N2oControl implements TextArea {

    @Override
    public void shouldBeEmpty() {
        element().should(Condition.empty);
    }

    @Override
    public String getValue() {
        return element().text();
    }

    @Override
    public void setValue(String value) {
        element().setValue(value);
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        WebElementCondition condition = value == null || value.isEmpty() ? Condition.empty : Condition.text(value);
        should(condition, duration);
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        element().should(Condition.attribute("placeholder", value));
    }

}
