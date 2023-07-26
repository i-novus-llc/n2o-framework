package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Абстрактная реализация компонента для автотестирования
 */
public abstract class N2oComponent extends N2oElement implements Component {

    @Override
    public void shouldExists() {
        element().should(Condition.exist);
    }

    @Override
    public void shouldNotExists(Duration... duration) {
        should(Condition.not(Condition.exist), duration);
    }

    @Override
    public void shouldBeVisible() {
        element().shouldBe(Condition.visible);
    }

    @Override
    public void shouldBeHidden() {
        element().shouldBe(Condition.hidden);
    }

    @Override
    public void shouldHaveCssClass(String cssClass) {
        element().shouldHave(Condition.cssClass(cssClass));
    }
}
