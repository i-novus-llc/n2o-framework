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
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveDashedLabel() {
        element().$(".list-text-cell__trigger_dashed").shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveDashedLabel() {
        element().$(".list-text-cell__trigger_dashed").shouldNotBe(Condition.exist);
    }

    @Override
    public void hover() {
        SelenideElement elm = cellTrigger();
        if (elm.is(Condition.exist))
            cellTrigger().hover();
        else element().hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().$(".tooltip-container");
        return N2oSelenide.component(element, Tooltip.class);
    }

    @Override
    public void click() {
        element().scrollTo();
        SelenideElement elm = cellTrigger();
        if (elm.is(Condition.exist))
            cellTrigger().click();
        else element().click();
    }

    private SelenideElement cellTrigger() {
        return element().$(".list-text-cell__trigger, .list-text-cell__trigger_dashed");
    }
}
