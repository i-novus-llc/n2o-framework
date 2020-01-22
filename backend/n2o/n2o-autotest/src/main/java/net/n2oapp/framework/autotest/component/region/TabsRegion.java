package net.n2oapp.framework.autotest.component.region;

import net.n2oapp.framework.autotest.N2oSelector;
import net.n2oapp.framework.autotest.factory.Widgets;

public interface TabsRegion extends Region {
    TabItem tab(int index);
    TabItem tab(N2oSelector by);
    Widgets activeTab();

    interface TabItem {
        void click();
        void shouldBeActive();
    }
}
