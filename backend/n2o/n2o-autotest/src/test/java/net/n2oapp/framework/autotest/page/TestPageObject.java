package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.TestRegion;
import net.n2oapp.framework.autotest.api.TestWidget;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;

public class TestPageObject {
    private LeftRightPage n2o = N2oSelenide.page(LeftRightPage.class);

    public TestWidget getLeftWidget0() {
        return n2o.left().region(0, TestRegion.class).content().widget(TestWidget.class);
    }

    public TestWidget getLeftWidget1() {
        return n2o.left().region(0, TestRegion.class).content().widget(1, TestWidget.class);
    }

    public TestWidget getRightWidget0() {
        return n2o.right().region(0, TestRegion.class).content().widget(TestWidget.class);
    }
}
