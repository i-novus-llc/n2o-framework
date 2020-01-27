package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.control.Control;

public interface Controls extends ComponentsCollection {
    <T extends Control> T control(Class<T> componentClass);

    <T extends Control> T control(int index, Class<T> componentClass);
}
