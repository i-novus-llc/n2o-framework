package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;

import java.time.Duration;

/**
 * Ячейка таблицы с текстом для автотестирования
 */
public class N2oBadgeCell extends N2oCell implements BadgeCell {
    @Override
    public void shouldHaveColor(Colors color) {
        element().$(".badge")
                .shouldHave(Condition.cssClass(color.name("badge-")));
    }

    @Override
    public void badgeShouldHaveText(String text, Duration... duration) {
        should(Condition.text(text), duration);
    }
}
