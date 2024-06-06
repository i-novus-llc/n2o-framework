package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.api.component.fieldset.FieldSet;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Филдсет для автотестирования
 */
public abstract class N2oFieldSet extends N2oComponent implements FieldSet {

    @Override
    public void shouldHaveDescription(String description, Duration... duration) {
        should(Condition.text(description), description(), duration);
    }

    @Override
    public void shouldNotHaveDescription() {
        description().shouldNot(Condition.exist);
    }

    @Override
    public void badgeShouldHavePosition(BadgePosition position) {
        if (position.equals(BadgePosition.LEFT))
            element().$(".n2o-badge-container").shouldHave(Condition.cssClass("flex-row-reverse"));
        else
            element().$(".n2o-badge-container").shouldNotHave(Condition.cssClass("flex-row-reverse"));
    }

    protected SelenideElement description() {
        return element().$(".n2o-fieldset__description");
    }
}
