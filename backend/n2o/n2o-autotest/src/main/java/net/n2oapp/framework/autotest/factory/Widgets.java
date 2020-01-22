package net.n2oapp.framework.autotest.factory;

import net.n2oapp.framework.autotest.component.widget.Widget;

public class Widgets {

    public <T extends Widget> T widget(int index, Class<T> componentClass) {
        return null;
    }

    public <T extends Widget> T widget(Class<T> componentClass) {
        return widget(0, componentClass);
    }
}
