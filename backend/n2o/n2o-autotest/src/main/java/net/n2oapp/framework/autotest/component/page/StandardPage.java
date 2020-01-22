package net.n2oapp.framework.autotest.component.page;

import net.n2oapp.framework.autotest.factory.Regions;
import net.n2oapp.framework.autotest.factory.Toolbar;

public interface StandardPage extends Page {

    PageToolbar toolbar();
    Regions place(String place);

    interface PageToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}
