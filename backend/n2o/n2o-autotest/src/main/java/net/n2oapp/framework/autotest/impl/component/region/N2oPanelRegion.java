package net.n2oapp.framework.autotest.impl.component.region;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;

public class N2oPanelRegion extends N2oRegion implements PanelRegion {
    @Override
    public Widgets content() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
    }

    @Override
    public void collapse() {

    }

    @Override
    public void expand() {

    }

    @Override
    public void shouldBeExpanded() {

    }

    @Override
    public void shouldBeCollapsed() {

    }
}
