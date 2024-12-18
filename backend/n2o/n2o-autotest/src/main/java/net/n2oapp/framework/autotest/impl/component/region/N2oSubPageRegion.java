package net.n2oapp.framework.autotest.impl.component.region;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.region.SubPageRegion;

/**
 * Регион подстраниц для автотестирования
 */
public class N2oSubPageRegion extends N2oRegion implements SubPageRegion {

    @Override
    public <T extends Page> T content(Class<T> pageClass) {
        return N2oSelenide.component(element().$(".n2o-page-body .n2o-page-body"), pageClass);
    }
}
