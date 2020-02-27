package net.n2oapp.framework.autotest.impl.component.header;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public class N2oDropdownMenuItem extends N2oMenuItem implements DropdownMenuItem {
    @Override
    public AnchorMenuItem item(int index) {
        return N2oSelenide.component(element().$$(".dropdown-menu li").get(index), N2oAnchorMenuItem.class);
    }
}
