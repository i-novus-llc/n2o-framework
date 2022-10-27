package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
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
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldHaveLabel(String label) {
        element().shouldHave(Condition.text(label));
    }

    @Override
    public void shouldHaveIcon(String name) {
        element().$(".n2o-icon").shouldHave(Condition.cssClass(name));
    }

    @Override
    public void shouldHaveColor(Colors color) {
        element().shouldHave(Condition.cssClass(color.name("btn-")));
    }

    @Override
    public void hover() {
        element().hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().parent().parent().parent().parent().$(".tooltip-container");
        return N2oSelenide.component(element, Tooltip.class);
    }
}
