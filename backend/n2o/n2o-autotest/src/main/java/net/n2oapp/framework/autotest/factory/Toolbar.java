package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.button.Button;
import net.n2oapp.framework.autotest.component.button.DropdownButton;
import net.n2oapp.framework.autotest.component.button.StandardButton;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class Toolbar extends N2oComponentsCollection {
    public Toolbar(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public StandardButton button(String label) {
        return null;
    }

    StandardButton button(Condition by) {
        return null;
    }

    DropdownButton dropdown(String label) {
        return null;
    }

    DropdownButton dropdown(Condition by) {
        return null;
    }

    <T extends Button> T button(String label, Class<T> componentClass) {
        return null;
    }

    <T extends Button> T button(Condition by, Class<T> componentClass) {
        return null;
    }
}
