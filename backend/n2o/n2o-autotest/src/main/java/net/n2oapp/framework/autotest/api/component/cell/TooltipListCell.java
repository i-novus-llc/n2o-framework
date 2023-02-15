package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.Tooltip;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком для автотестирования
 */
public interface TooltipListCell extends Cell {

    /**
     * Проверка соответствия текста
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text);

    /**
     * Проверка подчеркнутости заголовока пунктиром
     */
    void shouldHaveDashedLabel();

    /**
     * Проверка не подчеркнутости заголовока пунктиром
     */
    void shouldNotHaveDashedLabel();

    /**
     * Наведение мыши на ячейку
     */
    void hover();

    /**
     * @return Компонент тултип для автотестирования
     */
    Tooltip tooltip();

    /**
     * Клик по ячейке
     */
    void click();
}
