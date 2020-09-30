package net.n2oapp.framework.autotest.impl.component.region;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;

/**
 * Простой регион для автотестирования
 */
public class N2oSimpleRegion extends N2oRegion implements SimpleRegion {
    @Override
    public RegionItems content() {
        return N2oSelenide.collection(firstLevelElements(".n2o-none-region", "div"), RegionItems.class);
    }
}
