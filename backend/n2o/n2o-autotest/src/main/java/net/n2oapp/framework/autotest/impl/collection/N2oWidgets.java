package net.n2oapp.framework.autotest.impl.collection;

import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.widget.Widget;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oWidgets extends N2oComponentsCollection implements Widgets {

    public <T extends Widget> T widget(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    public <T extends Widget> T widget(Class<T> componentClass) {
        return component(elements().first(), componentClass);
    }
}
