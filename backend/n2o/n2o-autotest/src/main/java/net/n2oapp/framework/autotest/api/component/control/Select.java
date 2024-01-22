package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.PopupControl;

import java.time.Duration;

/**
 * Компонент выбора из выпадающего списка для автотестирования
 */
public interface Select extends Control, PopupControl {

    /**
     * Клик по полю
     */
    void click();

    /**
     * Ввод значения в поле
     * @param value значение для ввода
     */
    void setValue(String value);

    /**
     * Нажатие кнопки 'enter'
     */
    void pressEnter();

    /**
     * Очистка поля ввода
     */
    void clear();

    /**
     * Проверка того, что поле очищаемо
     */
    void shouldBeCleanable();

    /**
     * Проверка того, что поле не очищаемо
     */
    void shouldNotBeCleanable();

    /**
     * Проверка наличия выбранной опции с ожидаемым значением
     * @param value ожидаемое значение
     */
    void shouldSelected(String value, Duration... duration);

    /**
     * @return выпадающий список для автотестирования
     */
    DropDown dropdown();
}
