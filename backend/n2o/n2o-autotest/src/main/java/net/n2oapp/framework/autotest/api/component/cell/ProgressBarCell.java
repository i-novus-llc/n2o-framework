package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.autotest.Colors;

/**
 * Ячейка таблицы с ProgressBar для автотестирования
 */
public interface ProgressBarCell extends Cell {

    /**
     * Проверка соответствия цвета ячейки
     * @param color ожидаемый цвет
     */
    void shouldHaveColor(Colors color);

    /**
     * Проверка соответствия текста внутри ячейки
     * @param value ожидаемый текст
     */
    void shouldHaveValue(String value);

    /**
     * Проверка соответствия размера ячейки
     * @param size ожидаемый размер
     */
    void shouldHaveSize(Size size);

    /**
     * Проверка того, что ячейка с анимацией загрузки
     */
    void shouldBeAnimated();

    /**
     * Проверка того, что ячейка с полосками
     */
    void shouldBeStriped();

    /**
     * {@link N2oProgressBarCell.Size}
     */
    enum Size {
        small, normal, large
    }
}
