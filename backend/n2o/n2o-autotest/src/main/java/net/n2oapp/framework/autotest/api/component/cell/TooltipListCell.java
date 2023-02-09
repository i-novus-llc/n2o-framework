package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.Tooltip;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком для автотестирования
 */
public interface TooltipListCell extends Cell {
    void shouldHaveText(String text);

    void shouldHaveDashedLabel();

    void shouldNotHaveDashedLabel();

    void hover();

    Tooltip tooltip();

    void click();
}
