package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Компоненты ввода для автотестирования
 */
public class N2oControl extends N2oComponent implements Control {
    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldBe(Condition.disabled);
    }
}
