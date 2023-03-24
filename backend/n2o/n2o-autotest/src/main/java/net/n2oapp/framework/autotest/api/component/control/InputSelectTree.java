package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода с выбором в выпадающем списке в виде дерева для автотестирования
 */
public interface InputSelectTree extends Control, PopupControl {

    /**
     * Проверка соответствия текста подсказки для ввода
     * @param value ожидаемый текст
     */
    void shouldHavePlaceholder(String value);

    /**
     * Раскрытие выпадающего списка
     */
    void click();

    /**
     * Раскрытие списка у опции
     * @param parentId номер родительской опции
     */
    void expandParentOptions(int parentId);

    /**
     * Установка значения в поле ввода для поиска опции
     * @param value значение
     */
    void setFilter(String value);

    /**
     * Очищение поля поиска
     */
    void clearSearchField();

    /**
     * Проверка того, что опция отображаемые опции соответствуют условию
     * @param condition проверяемое условие
     */
    void shouldDisplayedOptions(CollectionCondition condition);

    /**
     * Выбор опции по номеру
     * @param index номер выбираемой опции
     */
    void selectOption(int index);

    /**
     * Проверка соответствия значения у выбранной опции
     * @param index номер проверяемой опции
     * @param value ожидаемое значение
     */
    void shouldBeSelected(int index, String value);

    /**
     * Удаление выбранной опции из поля ввода
     * @param index номер удаляемой опции
     */
    void removeOption(int index);

    /**
     * Удаление всех выбранных опций
     */
    void removeAllOptions();

    /**
     * Проверка того, что ни одна опция не выбрана
     */
    void shouldBeUnselected();

    /**
     * @return Выпадающий список в виде дерева для автотестирования
     */
    DropDownTree dropdown();
}
