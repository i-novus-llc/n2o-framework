package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с radio для автотестирования
 */

public interface RadioCell extends Cell {

    /**
     * Клик по ячейке
     */
    void click();

    /**
     * Проверка выбранности ячейки
     */
    void shouldBeChecked();

    /**
     * Проверка не выбранности ячейки
     */
    void shouldBeUnchecked();
}
