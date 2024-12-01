package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;

import java.util.Arrays;

public class N2oButtonField extends N2oField implements ButtonField {
    @Override
    public void click() {
        element().click();
    }

    @Override
    public void shouldBeEnabled() {
        btn().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        btn().shouldBe(Condition.disabled);
    }

    protected SelenideElement btn() {
        return element().$(".btn");
    }

    @Override
    public void shouldHaveIcon(String iconName) {
        Arrays.stream(iconName.split(" ")).forEach(i -> element().$("i").shouldHave(Condition.cssClass(i)));
    }

    @Override
    public void shouldHaveColor(Colors color) {
        btn().shouldHave(Condition.cssClass(color.name("btn-")));
    }

    @Override
    public void shouldHaveStyle(String style) {
        btn().shouldHave(Condition.attribute("style", style));
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
    public void tooltipShouldHavePosition(String position) {
        btn().shouldHave(Condition.attribute("hintposition", position));
    }
    @Override
    public void badgeShouldHavePosition(BadgePosition position) {
        btn().shouldHave(Condition.cssClass(position.name("btn-badge-position--")));
    }
}
