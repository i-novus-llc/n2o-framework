package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода текста с выбором из выпадающего списка для автотестирования
 */
public interface InputSelect extends Control, PopupControl {

    void click();

    void setValue(String value);

    void setMultiValue(String... values);

    void shouldHaveOptions(String... options);

    void select(int index);

    void select(Condition by);

    void selectMulti(int... indexes);

    void clear();

    void clearItems(String... items);

    void shouldSelected(String value);

    void shouldSelectedMulti(String... values);

    void shouldHaveOptionDescription(String option, String description);

    void shouldHaveItemWithStatusColor(String value, Colors color);

    void shouldHaveEnableItem(Boolean enabled, String itemValue);

    DropDown dropdown();
}
