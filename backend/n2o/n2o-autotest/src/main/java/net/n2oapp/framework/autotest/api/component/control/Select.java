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

    /**
     * Ввод значения в поле
     * @param value значение для ввода
     */
    void setValue(String value);

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
    void shouldSelected(String value);

    /**
     * @return выпадающий список для автотестирования
     */
    DropDown dropdown();

    /**
     * Следует использовать dropdown.shouldHaveOptions(String... options)
     */
    @Deprecated
    void shouldHaveOptions(String... options);

    /**
     * Следует использовать dropdown.select(int index)
     */
    @Deprecated
    void select(int index);

    /**
     * Следует использовать dropdown.select(Condition by)
     */
    @Deprecated
    void select(Condition by);

    /**
     * Следует использовать dropdown.selectMulti(int... indexes)
     */
    @Deprecated
    void selectMulti(int... indexes);

    /**
     * Следует использовать dropdown.shouldBeChecked(int... indexes)
     */
    @Deprecated
    void shouldBeChecked(int... indexes);

    /**
     * Следует использовать dropdown.shouldNotBeChecked(int... indexes)
     */
    @Deprecated
    void shouldNotBeChecked(int... indexes);


    /**
     * Следует использовать dropdown.optionShouldHaveDescription(String option, String description)
     */
    @Deprecated
    void optionShouldHaveDescription(String option, String description);
}
