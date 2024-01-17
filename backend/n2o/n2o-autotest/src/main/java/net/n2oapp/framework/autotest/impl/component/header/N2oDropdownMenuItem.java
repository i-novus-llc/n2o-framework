package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public class N2oDropdownMenuItem extends N2oMenuItem implements DropdownMenuItem {

    @Override
    public AnchorMenuItem item(int index) {
        return item(index, N2oAnchorMenuItem.class);
    }

    @Override
    public <T extends MenuItem> T item(int index, Class<T> componentClass) {
        return N2oSelenide.component(element().$$(".dropdown-menu li, .n2o-sidebar__subitems  .n2o-sidebar__item-title").get(index), componentClass);
    }

    @Override
    public void shouldHaveSize(int size) {
        element().$$(".dropdown-menu > .dropdown-item").shouldHave(CollectionCondition.size(size));
    }
}

