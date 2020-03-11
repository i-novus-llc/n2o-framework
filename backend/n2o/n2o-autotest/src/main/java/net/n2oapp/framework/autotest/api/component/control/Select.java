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

    void shouldBeEmpty();

    void clear();

    void shouldBeCleanable();

    void shouldNotBeCleanable();

    void shouldSelected(String value);
}
