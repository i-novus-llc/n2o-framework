package net.n2oapp.framework.autotest.api.component.button;

import com.codeborne.selenide.Condition;

/**
 * Кнопка с выпадающим меню для автотестирования
 */
public interface DropdownButton extends Button {
    StandardButton menuItem(String label);
    StandardButton menuItem(Condition by);
}
