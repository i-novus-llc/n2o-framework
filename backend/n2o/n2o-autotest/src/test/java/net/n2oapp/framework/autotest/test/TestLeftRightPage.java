package net.n2oapp.framework.autotest.test;

import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import static net.n2oapp.framework.autotest.N2oSelenide.collection;

public class TestLeftRightPage extends N2oComponent implements LeftRightPage {

    @Override
    public Regions left() {
        return collection(element().$(".left").$$(".region"), Regions.class);
    }

    @Override
    public Regions right() {
        return collection(element().$(".right").$$(".region"), Regions.class);
    }

    @Override
    public PageToolbar toolbar() {
        return null;
    }

}
