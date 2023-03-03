package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с Checkbox для автотестирования
 */
public interface CheckboxCell extends Cell {
    /**
     * Возвращает состояние выбранности ячейки
     * @return true - выбрана, false - не выбрана
     */
    boolean isChecked();

    /**
     * Установка выбранности ячейки
     * @param val значение устанавливаемое в чекбокс
     */
    void setChecked(boolean val);

    /**
     * Проверка того, что ячейка выбрана
     */
    void shouldBeChecked();

    /**
     * Проверка того, что ячейка не выбрана
     */
    void shouldBeUnchecked();

    /**
     * Проверка того, что ячейка доступна
     */
    void shouldBeEnabled();

    /**
     * Проверка того, что ячейка не доступна
     */
    void shouldBeDisabled();
}
