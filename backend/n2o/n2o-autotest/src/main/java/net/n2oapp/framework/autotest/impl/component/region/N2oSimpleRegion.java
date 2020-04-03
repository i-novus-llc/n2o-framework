package net.n2oapp.framework.autotest.impl.component.region;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;

/**
 * Простой регион для автотестирования
 */
public class N2oSimpleRegion extends N2oRegion implements SimpleRegion {
    @Override
    public Widgets content() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
    }
}
