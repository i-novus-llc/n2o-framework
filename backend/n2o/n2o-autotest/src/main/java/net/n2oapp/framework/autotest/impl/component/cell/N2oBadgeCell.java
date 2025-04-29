package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.BadgePositionEnum;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;

import java.time.Duration;

/**
 * Ячейка таблицы с текстом для автотестирования
 */
public class N2oBadgeCell extends N2oCell implements BadgeCell {

    private static final String BADGE_LOCATOR = ".badge";

    @Override
    public void shouldHaveColor(ColorsEnum color) {
        element().$(BADGE_LOCATOR)
                .shouldHave(Condition.cssClass(color.name("badge-")));
    }

    @Override
    public void badgeShouldHaveText(String text, Duration... duration) {
        should(Condition.text(text), duration);
    }

    @Override
    public void badgeShouldHavePosition(BadgePositionEnum position) {
        if (position.equals(BadgePositionEnum.LEFT))
            element().$(BADGE_LOCATOR).shouldHave(Condition.cssValue("order", "-1"));
        else
            element().$(BADGE_LOCATOR).shouldHave(Condition.cssValue("order", "1"));
    }
}
