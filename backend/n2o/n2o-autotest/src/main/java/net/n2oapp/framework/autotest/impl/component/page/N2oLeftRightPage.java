package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;

/**
 * Страница с левой и правой колонками для автотестирования
 */
public class N2oLeftRightPage extends N2oPage implements LeftRightPage {
    @Override
    public Regions left() {
        return N2oSelenide.collection(element().$$(".n2o-page__left .n2o-panel-region"), N2oRegions.class);
    }

    @Override
    public Regions right() {
        return N2oSelenide.collection(element().$$(".n2o-page__right .n2o-panel-region"), N2oRegions.class);
    }

}
