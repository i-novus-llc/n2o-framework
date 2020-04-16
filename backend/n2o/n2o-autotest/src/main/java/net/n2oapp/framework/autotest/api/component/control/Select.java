package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;

/**
 * Компонент выбора из выпадающего списка для автотестирования
 */
public interface Select extends Control {
    void openOptions();

    void closeOptions();

    void find(String query);

    void select(int index);

    void select(Condition by);

    void clear();

    void shouldBeCleanable();

    void shouldNotBeCleanable();

    void selectMulti(int... indexes);

    void shouldSelected(String value);

    void shouldBeChecked(int... indexes);

    void shouldNotBeChecked(int... indexes);
}
