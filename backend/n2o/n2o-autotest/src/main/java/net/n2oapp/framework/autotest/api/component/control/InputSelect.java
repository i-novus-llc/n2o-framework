package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода текста с выбором из выпадающего списка для автотестирования
 */
public interface InputSelect extends Control, PopupControl {

    /**
     * Клик по полю ввода
     */
    void click();

    /**
     * Установка значения в поле ввода
     * @param value вводимое значение
     */
    void setValue(String value);

    /**
     * Установка нескольких значений в поле ввода
     * @param values список вводимых значений
     */
    void setMultiValue(String... values);

    /**
     * Очистка поля ввода
     */
    void clear();

    /**
     * Удаление выбранных опций по номеру
     * @param items номера удаляемых опций
     */
    void clearItems(String... items);

    /**
     * Проверка наличия выбранных опций с ожидаемыми значениями
     * @param values список ожидаемых значений
     */
    void shouldSelectedMulti(String... values);

    /**
     * @return выпадающий список для автотестирования
     */
    DropDown dropdown();
}
