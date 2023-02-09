package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;

/**
 * Ячейка с текстом для автотестирования
 */
public class N2oTextCell extends N2oCell implements TextCell {

    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveSubText(String... text) {
        element().$$(".text-muted").shouldHave(CollectionCondition.texts(text));
    }

    @Override
    public void shouldHaveIconPosition(Position position) {
        SelenideElement iconContainer = element().$(".icon-cell-container");
        if (iconContainer.$(".n2o-icon").is(Condition.exist)) {
            if (position == Position.LEFT)
                iconContainer.shouldNotHave(Condition.cssClass("icon-cell-container__text-left"));
            if (position == Position.RIGHT)
                iconContainer.shouldHave(Condition.cssClass("icon-cell-container__text-left"));
        }
    }
}
