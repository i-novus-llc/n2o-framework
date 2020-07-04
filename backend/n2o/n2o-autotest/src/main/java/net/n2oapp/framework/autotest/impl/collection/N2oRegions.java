package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.region.Region;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oRegions extends N2oComponentsCollection implements Regions {

    public <T extends Region> T region(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    public <T extends Region> T region(Condition findBy, Class<T> componentClass) {
        return component(elements().findBy(findBy), componentClass);
    }
}
