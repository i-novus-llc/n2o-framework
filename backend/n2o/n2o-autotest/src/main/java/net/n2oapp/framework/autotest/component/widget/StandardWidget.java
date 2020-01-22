package net.n2oapp.framework.autotest.component.widget;

import net.n2oapp.framework.autotest.factory.Toolbar;

public interface StandardWidget extends Widget {
    WidgetToolbar toolbar();

    interface WidgetToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}
