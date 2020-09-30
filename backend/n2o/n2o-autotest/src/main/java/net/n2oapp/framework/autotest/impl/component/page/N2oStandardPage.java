package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;

import static net.n2oapp.framework.autotest.N2oSelenide.collection;

public class N2oStandardPage extends N2oPage implements StandardPage {
    @Override
    public Regions place(String place) {
        switch (place) {
            case "right":
                return collection(element().$$("div.n2o-page__right > div"), N2oRegions.class);
            case "left":
                return collection(element().$$("div.n2o-page__left > div"), N2oRegions.class);
            case "single":
                return collection(element().$$("div.n2o-page__single > div"), N2oRegions.class);
        }
        return null;
    }
}
