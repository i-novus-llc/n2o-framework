package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;

import static net.n2oapp.framework.autotest.N2oSelenide.collection;

public class N2oStandardPage extends N2oPage implements StandardPage {
    @Override
    public Regions regions() {
        return collection(element().$$("div.n2o-page__single > div, div.n2o-page__single > section"), N2oRegions.class);
    }
}
