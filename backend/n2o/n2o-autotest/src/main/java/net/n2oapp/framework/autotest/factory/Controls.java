package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.control.Control;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class Controls extends N2oComponentsCollection {

    public Controls(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public <T extends Control> T control(Class<T> componentClass) {
        return null;
    }
}
