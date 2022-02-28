package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Абстрактная реализация компонента для автотестирования
 */
public abstract class N2oComponent extends N2oElement implements Component {

    @Override
    public void shouldExists() {
        element().should(Condition.exist);
    }

    @Override
    public void shouldNotExists() {
        element().shouldNot(Condition.exist);
    }

    @Override
    public void shouldBeVisible() {
        element().shouldBe(Condition.visible);
    }

    @Override
    public void shouldBeHidden() {
        element().shouldBe(Condition.hidden);
    }

}
