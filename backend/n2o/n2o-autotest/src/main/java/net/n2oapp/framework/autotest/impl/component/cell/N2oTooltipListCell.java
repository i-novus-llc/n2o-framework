package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.cell.TooltipListCell;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком для автотестирования
 */
public class N2oTooltipListCell extends N2oCell implements TooltipListCell {
    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.exactText(text));
    }

    @Override
    public void shouldHaveDashedLabel() {
        dashedLabel().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveDashedLabel() {
        dashedLabel().shouldNotBe(Condition.exist);
    }

    @Override
    public void hover() {
        SelenideElement cellTrigger = cellTrigger();
        if (cellTrigger.is(Condition.exist))
            cellTrigger.hover();
        else
            element().hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().$(".tooltip-container");
        return N2oSelenide.component(element, Tooltip.class);
    }

    @Override
    public void click() {
        element().scrollTo();
        SelenideElement cellTrigger = cellTrigger();
        if (cellTrigger.is(Condition.exist))
            cellTrigger.click();
        else
            element().click();
    }

    protected SelenideElement dashedLabel() {
        return element().$(".list-text-cell__trigger_dashed");
    }

    protected SelenideElement cellTrigger() {
        return element().$(".list-text-cell__trigger, .list-text-cell__trigger_dashed");
    }
}
