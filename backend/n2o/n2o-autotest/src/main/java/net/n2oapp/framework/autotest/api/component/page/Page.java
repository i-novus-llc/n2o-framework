package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;

public interface Page extends Component {
    PageToolbar toolbar();

    interface PageToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}
