package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
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
    public void shouldHaveIconPosition(PositionEnum position) {
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

    @Override
    public void hover() {
        element().$(".icon-cell-container").hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().parent().parent().parent().parent().$(".tooltip-container");
        return N2oSelenide.component(element, Tooltip.class);
    }
}
