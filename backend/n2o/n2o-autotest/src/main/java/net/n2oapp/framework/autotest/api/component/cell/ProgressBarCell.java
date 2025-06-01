package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.autotest.ColorsEnum;

/**
 * Ячейка таблицы с ProgressBar для автотестирования
 */
public interface ProgressBarCell extends Cell {

    /**
     * Проверка соответствия цвета ячейки
     *
     * @param color ожидаемый цвет
     */
    void shouldHaveColor(ColorsEnum color);

    /**
     * Проверка соответствия текста внутри ячейки
     *
     * @param value ожидаемый текст
     */
    void shouldHaveValue(String value);

    /**
     * Проверка соответствия размера ячейки
     *
     * @param size ожидаемый размер
     */
    void shouldHaveSize(SizeEnum size);

    /**
     * Проверка того, что ячейка с анимацией загрузки
     */
    void shouldBeAnimated();

    /**
     * Проверка того, что ячейка с полосками
     */
    void shouldBeStriped();

    /**
     * {@link N2oProgressBarCell.SizeEnum}
     */
    enum SizeEnum {
        SMALL,
        NORMAL,
        LARGE;
    }
}
