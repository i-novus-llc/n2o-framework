package net.n2oapp.framework.autotest.component.button;

import com.codeborne.selenide.Condition;

public interface DropdownButton extends Button {
    StandardButton menuItem(String label);
    StandardButton menuItem(Condition by);
}
