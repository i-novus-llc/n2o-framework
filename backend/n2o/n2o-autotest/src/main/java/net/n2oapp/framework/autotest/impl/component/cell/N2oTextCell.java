package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;

import java.time.Duration;

/**
 * Ячейка с текстом для автотестирования
 */
public class N2oTextCell extends N2oCell implements TextCell {

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.exactText(text), duration);
    }

    @Override
    public void shouldHaveSubText(String[] text, Duration... duration) {
        should(CollectionCondition.texts(text), element().$$(".text-muted"), duration);
    }

    @Override
    public void shouldHaveIconPosition(Position position) {
        SelenideElement iconContainer = element().$(".icon-cell-container");
        if (iconContainer.$(".n2o-icon").is(Condition.exist)) {
            switch (position) {
                case LEFT:
                    iconContainer.shouldNotHave(Condition.cssClass("icon-cell-container__text-left"));
                    break;
                case RIGHT:
                    iconContainer.shouldHave(Condition.cssClass("icon-cell-container__text-left"));
                    break;
            }
        }
    }
}
