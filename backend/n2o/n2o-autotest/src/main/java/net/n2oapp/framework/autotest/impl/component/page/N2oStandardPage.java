package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;

public class N2oStandardPage extends N2oPage implements StandardPage {

    @Override
    public Regions place(String place) {
        return N2oSelenide.collection(element().$$(".n2o-page__" + place), N2oRegions.class);
    }

    @Override
    public Widgets widgets() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
    }
}
