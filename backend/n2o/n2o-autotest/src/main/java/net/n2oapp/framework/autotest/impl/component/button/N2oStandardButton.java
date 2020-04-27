package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

/**
 * Стандартная кнопка для автотестирования
 */
public class N2oStandardButton extends N2oButton implements StandardButton {

    @Override
    public void shouldBeDisabled() {
        element().shouldBe(Condition.attribute("disabled"));
    }

    @Override
    public void shouldBeEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldHaveValue(String value) {
        shouldHaveLabel(value);
    }

    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldHaveLabel(String label) {
        element().shouldHave(Condition.text(label));
    }
}
