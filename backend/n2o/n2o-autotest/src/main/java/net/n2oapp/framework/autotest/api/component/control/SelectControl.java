package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;

/**
 * Компонент выбора из выпадающего списка (select) для автотестирования
 */
public interface SelectControl extends Control {
    void openOptions();

    void closeOptions();

    void find(String query);

    void select(int index);

    void select(Condition by);

    void clear();

    void shouldBeClearable();

    void shouldNotBeClearable();

    void shouldSelected(String value);
}
