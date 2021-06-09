package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Dropdown;

/**
 * Компонент ввода текста с выбором из выпадающего списка для автотестирования
 */
public interface InputSelect extends Control, Dropdown {

    void click();

    void val(String value);

    void valMulti(String... values);

    void shouldHaveOptions(String... options);

    void select(int index);

    void select(Condition by);

    void selectMulti(int... indexes);

    void clear();

    void clearItems(String... items);

    void shouldSelected(String value);

    void shouldSelectedMulti(String... values);

    void optionShouldHaveDescription(String option, String description);

    void itemShouldHaveStatusColor(String value, Colors color);

    void itemShouldBeEnabled(Boolean enabled, String itemValue);
}
