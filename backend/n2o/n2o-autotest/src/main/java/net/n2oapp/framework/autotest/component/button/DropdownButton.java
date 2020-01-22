package net.n2oapp.framework.autotest.component.button;

import net.n2oapp.framework.autotest.N2oSelector;

public interface DropdownButton extends Button {
    StandardButton menuItem(String label);
    StandardButton menuItem(N2oSelector by);
}
