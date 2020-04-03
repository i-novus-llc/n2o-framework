package net.n2oapp.framework.autotest.impl.collection;

import net.n2oapp.framework.autotest.api.collection.Controls;
import net.n2oapp.framework.autotest.api.component.control.Control;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oControls extends N2oComponentsCollection implements Controls {

    public <T extends Control> T control(Class<T> componentClass) {
        return component(elements().first(), componentClass);
    }

    public <T extends Control> T control(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }
}
