package net.n2oapp.framework.autotest.component.page;

import net.n2oapp.framework.autotest.component.Component;
import net.n2oapp.framework.autotest.factory.Toolbar;

public interface Page extends Component {
    PageToolbar toolbar();

    interface PageToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}
