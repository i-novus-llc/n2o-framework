package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент чекбокса для автотестирования
 */
public interface Checkbox extends Control {
    /**
     * Проверка состояния чекбокса на выбранность
     * @return true - чекбокс выбран, false - чекбок не выбран
     */
    boolean isChecked();

    /**
     * Установка состояния чекбокса
     * @param val true или false, соответствующее чекбокс выбран или нет
     */
    void setChecked(boolean val);

    /**
     * Проверка того, что чекбокс выбран
     */
    void shouldBeChecked();
}
