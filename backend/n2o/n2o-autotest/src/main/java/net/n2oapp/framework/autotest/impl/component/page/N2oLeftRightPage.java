package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oLeftRightPage extends N2oComponent implements LeftRightPage {
    @Override
    public Regions left() {
        return null;
    }

    @Override
    public Regions right() {
        return null;
    }

    @Override
    public PageToolbar toolbar() {
        return null;
    }
}
