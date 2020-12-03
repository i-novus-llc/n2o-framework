package net.n2oapp.framework.autotest.impl.component.region;

import net.n2oapp.framework.autotest.api.component.region.Region;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.widget.Widget;
import net.n2oapp.framework.autotest.impl.collection.N2oComponentsCollection;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Элемент региона (виджет/регион) для автотестирования
 */
public class N2oRegionItems extends N2oComponentsCollection implements RegionItems {
    @Override
    public <T extends Widget> T widget(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    @Override
    public <T extends Widget> T widget(Class<T> componentClass) {
        return component(elements().first(), componentClass);
    }

    @Override
    public <T extends Region> T region(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    @Override
    public <T extends Region> T region(Class<T> componentClass) {
        return component(elements().first(), componentClass);
    }
}
