package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.collection.Toolbar;

public interface StandardWidget extends Widget {
    WidgetToolbar toolbar();

    interface WidgetToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}
