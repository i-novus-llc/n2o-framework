package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.region.Region;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class Regions extends N2oComponentsCollection {

    public Regions(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public <T extends Region> T region(int index, Class<T> componentClass) {
        return factory().component(elements().get(index), componentClass);
    }

    public <T extends Region> T region(Condition condition, Class<T> componentClass) {
        return null;
    }
}
