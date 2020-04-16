package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с radio для автотестирования
 */

public interface RadioCell extends Cell {

    void click();

    void shouldBeChecked();

    void shouldBeUnchecked();
}
