package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
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
     * Проверка опций в выпадающем списке
     * @param options список ожидаемых опций
     */
    void shouldHaveOptions(String... options);

    /**
     * Выбор опции из выпадающего списка по номеру
     * @param index номер выбираемой опции
     */
    void select(int index);

    /**
     * Выбор опции из выпадающего списка по условию
     * @param by условие выбора опции
     */
    void select(Condition by);

    /**
     * Выбор опций из выпадающего списка по номеру
     * @param indexes список номеров выбираемых опций
     */
    void selectMulti(int... indexes);

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
     * Проверка наличия выбранной опции с ожидаемым значением
     * @param value ожидаемое значение
     */
    void shouldSelected(String value);

    /**
     * Проверка наличия выбранных опций с ожидаемыми значениями
     * @param values список ожидаемых значений
     */
    void shouldSelectedMulti(String... values);

    /**
     * Проверка соответствия дополнительной информации у опции
     * @param option проверяемая опция
     * @param description ожидаемая дополнительная информация
     */
    void optionShouldHaveDescription(String option, String description);

    /**
     * Проверка соответствия цвета у опции
     * @param option проверяемая опция
     * @param color ожидаемый цвет
     */
    void optionShouldHaveStatusColor(String option, Colors color);

    /**
     * Проверка состояния доступности опции в выпадающем списке
     * @param enabled состояние
     * @param option проверяемая опция
     */
    void optionShouldBeEnabled(Boolean enabled, String option);

    /**
     * @return выпадающий список для автотестирования
     */
    DropDown dropdown();
}
