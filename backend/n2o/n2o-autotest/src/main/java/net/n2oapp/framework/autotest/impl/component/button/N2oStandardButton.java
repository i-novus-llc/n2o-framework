package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

import java.time.Duration;
import java.util.Arrays;

/**
 * Стандартная кнопка для автотестирования
 */
public class N2oStandardButton extends N2oButton implements StandardButton {

    @Override
    public void shouldHaveLabel(String label, Duration... duration) {
        should(Condition.exactText(label), duration);
    }

    @Override
    public void shouldHaveDescription(String description, Duration... duration) {
        should(Condition.text(description), duration);
    }

    @Override
    public void shouldHaveIcon(String iconName) {
        Arrays.stream(iconName.split(" ")).forEach(i -> element().$("i").shouldHave(Condition.cssClass(i)));
    }

    @Override
    public void shouldNotHaveIcon() {
            element().$("i").shouldNot(Condition.exist);
    }

    @Override
    public void shouldBeRounded() {
        element().shouldHave(Condition.cssClass("btn-rounded__with-content"));
    }

    @Override
    public void shouldNotBeRounded() {
        element().shouldNotHave(Condition.cssClass("btn-rounded__with-content"));
    }
}