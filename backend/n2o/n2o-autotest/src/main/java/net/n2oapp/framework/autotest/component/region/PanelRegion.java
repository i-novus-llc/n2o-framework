package net.n2oapp.framework.autotest.component.region;

import net.n2oapp.framework.autotest.factory.Widgets;

public interface PanelRegion extends Region {
    Widgets content();
    void collapse();
    void expand();

    void shouldBeExpanded();
    void shouldBeCollapsed();
}
