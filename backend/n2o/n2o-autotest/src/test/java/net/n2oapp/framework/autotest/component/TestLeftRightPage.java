package net.n2oapp.framework.autotest.component;

import net.n2oapp.framework.autotest.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.factory.Regions;
import net.n2oapp.framework.autotest.impl.N2oComponent;

public class TestLeftRightPage extends N2oComponent implements LeftRightPage {

    @Override
    public Regions left() {
        return new Regions(element().$(".left").$$(".region"), factory());
    }

    @Override
    public Regions right() {
        return new Regions(element().$(".right").$$(".region"), factory());
    }

    @Override
    public PageToolbar toolbar() {
        return null;
    }

}
