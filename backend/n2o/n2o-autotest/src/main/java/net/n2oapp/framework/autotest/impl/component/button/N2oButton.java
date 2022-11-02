package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Кнопка для автотестирования
 */
public abstract class N2oButton extends N2oComponent implements Button {

    @Override
    public void shouldBeDisabled() {
        element().shouldBe(Condition.attribute("disabled"));
    }

    @Override
    public void click() {
        element().shouldBe(Condition.exist).click();
    }
}
