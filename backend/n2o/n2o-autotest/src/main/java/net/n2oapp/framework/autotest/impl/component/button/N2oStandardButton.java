package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

/**
 * Стандартная кнопка для автотестирования
 */
public class N2oStandardButton extends N2oButton implements StandardButton {
    @Override
    public void click() {
        element().shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldBe(Condition.disabled);
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
