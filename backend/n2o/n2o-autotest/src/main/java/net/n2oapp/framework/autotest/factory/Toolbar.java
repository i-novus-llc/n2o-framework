package net.n2oapp.framework.autotest.factory;

import net.n2oapp.framework.autotest.N2oSelector;
import net.n2oapp.framework.autotest.component.button.Button;
import net.n2oapp.framework.autotest.component.button.DropdownButton;
import net.n2oapp.framework.autotest.component.button.StandardButton;

public class Toolbar {
    public StandardButton button(String label) {
        return null;
    }

    StandardButton button(N2oSelector by) {
        return null;
    }

    DropdownButton dropdown(String label) {
        return null;
    }

    DropdownButton dropdown(N2oSelector by) {
        return null;
    }

    <T extends Button> T button(String label, Class<T> componentClass) {
        return null;
    }

    <T extends Button> T button(N2oSelector by, Class<T> componentClass) {
        return null;
    }
}
