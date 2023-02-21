package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент выбора из выпадающего списка для автотестирования
 */
public interface Select extends Control, PopupControl {

    /**
     * Клик по полю
     */
    void click();

    void find(String query);

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
     * Выбор опций из выпадающего списка по номеру
     * @param indexes список номеров выбираемых опций
     */
    void selectMulti(int... indexes);

    /**
     * Проверка наличия выбранной опции с ожидаемым значением
     * @param value ожидаемое значение
     */
    void shouldSelected(String value);

    /**
     * Проверка того, что опции выбраны
     * @param indexes индексы проверяемых опций
     */
    void shouldBeChecked(int... indexes);

    /**
     * Проверка того, что опции не выбраны
     * @param indexes индексы проверяемых опций
     */
    void shouldNotBeChecked(int... indexes);


    /**
     * Проверка соответствия дополнительной информации у опции
     * @param option проверяемая опция
     * @param description ожидаемая дополнительная информация
     */
    void optionShouldHaveDescription(String option, String description);

    /**
     * @return выпадающий список для автотестирования
     */
    DropDown dropdown();
}
