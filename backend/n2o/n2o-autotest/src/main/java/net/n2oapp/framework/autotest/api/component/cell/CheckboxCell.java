package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с Checkbox для автотестирования
 */
public interface CheckboxCell extends Cell {
    boolean isChecked();

    void setChecked(boolean val);

    void shouldBeChecked();

    void shouldBeUnchecked();

    void shouldBeEnabled();

    void shouldBeDisabled();
}
