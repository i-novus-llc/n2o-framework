package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Кнопка для автотестирования
 */
public abstract class N2oButton extends N2oComponent implements Button {

    @Override
    public void shouldBeDisabled() {
        SelenideElement element = element();
        if (element.is(Condition.attribute("disabled")))
            return;
        element.$(".custom-control-input").shouldBe(Condition.disabled);
    }

    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void click() {
        element().shouldBe(Condition.exist, Condition.interactable).click();
    }

    @Override
    public void hover() {
        element().hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().parent()
                .parent().parent().parent()
                .$(".tooltip-container");

        return N2oSelenide.component(element, Tooltip.class);
    }

    @Override
    public void shouldHaveColor(ColorsEnum color) {
        element().shouldHave(Condition.cssClass(color.name("btn-")));
    }
}
