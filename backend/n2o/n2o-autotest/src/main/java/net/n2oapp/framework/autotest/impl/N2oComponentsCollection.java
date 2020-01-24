package net.n2oapp.framework.autotest.impl;

import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.ComponentsCollection;
import net.n2oapp.framework.autotest.factory.ComponentFactory;
import net.n2oapp.framework.autotest.factory.FactoryAware;

public class N2oComponentsCollection implements ComponentsCollection, FactoryAware {
    private ElementsCollection elements;
    private ComponentFactory factory;

    public N2oComponentsCollection(ElementsCollection elements, ComponentFactory factory) {
        this.elements = elements;
        this.factory = factory;
    }

    @Override
    public ElementsCollection elements() {
        return elements;
    }

    @Override
    public ComponentFactory factory() {
        return factory;
    }

    @Override
    public void setElements(ElementsCollection elements) {
        this.elements = elements;
    }

    @Override
    public void setFactory(ComponentFactory factory) {
        this.factory = factory;
    }
}
