package net.n2oapp.framework.autotest.api.component.cell;

import java.time.Duration;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком для автотестирования
 */
public interface TooltipListCell extends Cell {

    /**
     * Проверка точного соответствия (без учета регистра) текста
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text, Duration... duration);

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
     * Клик по ячейке
     */
    void click();
}
