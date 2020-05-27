package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;

/**
 * Компонент ввода текста с выбором из выпадающего списка для автотестирования
 */
public interface InputSelect extends Control {

    void val(String value);

    void valMulti(String... values);

    void select(int index);

    void select(Condition by);

    void selectMulti(int... indexes);

    void clear();

    void clearItems(String... items);

    void shouldSelected(String value);

    void shouldSelectedMulti(String... values);

    void shouldBeDisabled(String value);

}
