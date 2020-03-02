package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;

/**
 * Компонент поля ввода текста с выбором из выпадающего списка (input-select) для автотестирования
 */
public interface InputSelectControl extends Control {

    void val(String value);

    void valMulti(String... values);

    void shouldHaveValue(String value);

    void select(int index);

    void select(Condition by);

    void selectMulti(int... indexes);

    void clear();

    void clearItems(String... items);

    void shouldSelected(String value);

    void shouldSelectedMulti(String... values);
}
