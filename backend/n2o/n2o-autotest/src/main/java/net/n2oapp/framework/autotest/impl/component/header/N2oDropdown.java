package net.n2oapp.framework.autotest.impl.component.header;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.header.Dropdown;
import net.n2oapp.framework.autotest.api.component.header.Link;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public class N2oDropdown extends N2oMenuItem implements Dropdown {
    @Override
    public Link item(int index) {
        return N2oSelenide.component(element().parent().$$(".dropdown-item .nav-link").get(index), N2oLink.class);
    }
}
