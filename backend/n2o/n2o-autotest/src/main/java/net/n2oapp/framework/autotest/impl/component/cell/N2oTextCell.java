package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;

/**
 * Ячейка с текстом для автотестирования
 */
public class N2oTextCell extends N2oCell implements TextCell {

    @Override
    public void textShouldHave(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void subTextShouldHave(String... text) {
        element().$$(".text-muted").shouldHave(CollectionCondition.texts(text));
    }

    @Override
    public void shouldBeIconPosition(IconPosition position) {
        SelenideElement iconContainer = element().$(".icon-cell-container");
        if (iconContainer.$(".n2o-icon").is(Condition.exist)) {
            if (position == IconPosition.left)
                iconContainer.shouldNotHave(Condition.cssClass("icon-cell-container__text-left"));
            if (position == IconPosition.right)
                iconContainer.shouldHave(Condition.cssClass("icon-cell-container__text-left"));
        }
    }

    public enum IconPosition {
        left, right
    }
}
