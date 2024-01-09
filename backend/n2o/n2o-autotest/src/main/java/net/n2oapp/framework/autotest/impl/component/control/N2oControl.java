package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Компоненты ввода для автотестирования
 */
public abstract class N2oControl extends N2oComponent implements Control {

    protected static final String INPUT = ".n2o-input";

    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldBe(Condition.disabled);
    }
}
