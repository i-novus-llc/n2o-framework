package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком для автотестирования
 */
public interface TooltipListCell extends Cell {
    void shouldHaveText(String text);

    void hover();

    void click();
}
