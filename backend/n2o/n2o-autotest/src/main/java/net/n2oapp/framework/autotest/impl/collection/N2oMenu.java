package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Меню для автотестирования
 */
public class N2oMenu extends N2oComponentsCollection implements Menu {

    @Override
    public AnchorMenuItem anchor(int index) {
        return item(index, AnchorMenuItem.class);
    }

    @Override
    public AnchorMenuItem anchor(Condition findBy) {
        return item(findBy, AnchorMenuItem.class);
    }

    @Override
    public DropdownMenuItem dropdown(int index) {
        return item(index, DropdownMenuItem.class);
    }

    @Override
    public DropdownMenuItem dropdown(Condition findBy) {
        return item(findBy, DropdownMenuItem.class);
    }

    @Override
    public <T extends MenuItem> T item(int index, Class<T> componentClass) {
        return N2oSelenide.component(elements().get(index), componentClass);
    }

    @Override
    public <T extends MenuItem> T item(Condition condition, Class<T> componentClass) {
        return N2oSelenide.component(elements().findBy(condition), componentClass);
    }
}
