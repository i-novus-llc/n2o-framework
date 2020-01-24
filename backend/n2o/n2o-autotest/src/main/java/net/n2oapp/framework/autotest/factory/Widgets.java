package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.widget.Widget;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class Widgets extends N2oComponentsCollection {

    public Widgets(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public <T extends Widget> T widget(int index, Class<T> componentClass) {
        return factory().component(elements().get(index), componentClass);
    }

    public <T extends Widget> T widget(Class<T> componentClass) {
        return widget(0, componentClass);
    }
}
