package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

public interface Widgets extends ComponentsCollection {
    <T extends Widget> T widget(int index, Class<T> componentClass);

    <T extends Widget> T widget(Class<T> componentClass);
}
