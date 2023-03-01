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
     * @return выпадающий список для автотестирования
     */
    DropDown dropdown();

    /**
     * Следует использовать dropdown().shouldHaveOptions(String... options)
     */
    @Deprecated
    void shouldHaveOptions(String... options);

    /**
     * Следует использовать dropdown().select(int index)
     */
    @Deprecated
    void select(int index);

    /**
     * Следует использовать dropdown().select(Condition by)
     */
    @Deprecated
    void select(Condition by);

    /**
     * Следует использовать dropdown().selectMulti(int... indexes)
     */
    @Deprecated
    void selectMulti(int... indexes);

    /**
     * Следует использовать dropdown().optionShouldHaveDescription(String option, String description)
     */
    @Deprecated
    void optionShouldHaveDescription(String option, String description);

    /**
     * Следует использовать dropdown().optionShouldHaveStatusColor(String option, Colors color)
     */
    @Deprecated
    void optionShouldHaveStatusColor(String option, Colors color);

    /**
     * Следует использовать dropdown().optionShouldBeEnabled(String option)
     */
    @Deprecated
    void optionShouldBeEnabled(String option);

    /**
     * Следует использовать dropdown().optionShouldBeDisabled(String option)
     */
    @Deprecated
    void optionShouldBeDisabled(String option);
}
