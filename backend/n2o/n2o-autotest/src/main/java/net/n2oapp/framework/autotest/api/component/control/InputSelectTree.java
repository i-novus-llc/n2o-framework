package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода с выбором в выпадающем списке в виде дерева для автотестирования
 */
public interface InputSelectTree extends Control, PopupControl {

    void shouldHavePlaceholder(String value);

    void expandOptions();

    void expandParentOptions(int parentId);

    void setFilter(String value);

    void shouldDisplayedOptions(CollectionCondition condition);

    void selectOption(int index);

    void shouldBeSelected(int index, String value);

    void removeOption(int index);

    void removeAllOptions();

    void shouldBeUnselected();

    DropDownTree dropdown();
}
