package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;

import java.time.Duration;

/**
 * Ячейка таблицы с текстом для автотестирования
 */
public class N2oBadgeCell extends N2oCell implements BadgeCell {

    private static final String BADGE_LOCATOR = ".badge";

    @Override
    public void shouldHaveColor(Colors color) {
        element().$(BADGE_LOCATOR)
                .shouldHave(Condition.cssClass(color.name("badge-")));
    }

    @Override
    public void badgeShouldHaveText(String text, Duration... duration) {
        should(Condition.text(text), duration);
    }

    @Override
    public void badgeShouldHavePosition(BadgePosition position) {
        if (position.equals(BadgePosition.LEFT))
            element().$(BADGE_LOCATOR).shouldHave(Condition.cssValue("order", "-1"));
        else
            element().$(BADGE_LOCATOR).shouldHave(Condition.cssValue("order", "1"));
    }
}
