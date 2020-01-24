package net.n2oapp.framework.autotest.component;

import net.n2oapp.framework.autotest.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.factory.Widgets;
import net.n2oapp.framework.autotest.impl.N2oComponent;

public class TestRegion extends N2oComponent implements SimpleRegion {

    @Override
    public Widgets content() {
        return new Widgets(element().$$(".widget"), factory());
    }
}
