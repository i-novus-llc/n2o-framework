package net.n2oapp.framework.autotest.impl.component.header;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public class N2oDropdownMenuItem extends N2oMenuItem implements DropdownMenuItem {
    @Deprecated
    @Override
    public AnchorMenuItem item(int index) {
        return N2oSelenide.component(element().$$(".dropdown-menu li").get(index), AnchorMenuItem.class);
    }

    @Override
    public <T extends MenuItem> T item(int index, Class<T> componentClass) {
        return N2oSelenide.component(element().$$(".dropdown-menu li").get(index), componentClass);
    }

    @Override
    public void shouldHaveSize(int size) {
        element().$$(".nav-item").shouldHaveSize(size);
    }
}

