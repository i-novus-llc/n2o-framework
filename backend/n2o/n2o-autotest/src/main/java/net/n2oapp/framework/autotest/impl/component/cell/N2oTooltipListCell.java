package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.TooltipListCell;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком для автотестирования
 */
public class N2oTooltipListCell extends N2oCell implements TooltipListCell {
    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void hover() {
        cellTrigger().hover();
    }

    @Override
    public void click() {
        cellTrigger().click();
    }

    private SelenideElement cellTrigger() {
        return element().$(".list-text-cell__trigger");
    }
}
