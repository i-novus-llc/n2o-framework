package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Элемент региона (виджет/регион) для автотестирования
 */
public interface RegionItems extends ComponentsCollection {
    <T extends Widget> T widget(int index, Class<T> componentClass);

    <T extends Widget> T widget(Class<T> componentClass);

    <T extends Region> T region(int index, Class<T> componentClass);

    <T extends Region> T region(Class<T> componentClass);
}
