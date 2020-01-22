package net.n2oapp.framework.autotest.component.page;

import net.n2oapp.framework.autotest.factory.Regions;

public interface LeftRightPage extends StandardPage {
    Regions left();
    Regions right();
}
