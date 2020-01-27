package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.collection.Widgets;

public interface PanelRegion extends Region {
    Widgets content();
    void collapse();
    void expand();

    void shouldBeExpanded();
    void shouldBeCollapsed();
}
