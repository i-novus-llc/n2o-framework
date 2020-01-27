package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

public class N2oToolbar extends N2oComponentsCollection implements Toolbar {

    public StandardButton button(String label) {
        return null;
    }

    public StandardButton button(Condition findBy) {
        return null;
    }

    public DropdownButton dropdown(String label) {
        return null;
    }

    public DropdownButton dropdown(Condition findBy) {
        return null;
    }

    public <T extends Button> T button(String label, Class<T> componentClass) {
        return null;
    }

    public <T extends Button> T button(Condition findBy, Class<T> componentClass) {
        return null;
    }
}
