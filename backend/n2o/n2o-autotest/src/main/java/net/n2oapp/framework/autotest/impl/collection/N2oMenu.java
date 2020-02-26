package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Кнопки хедера для автотестирования
 */
public class N2oMenu extends N2oComponentsCollection implements Menu {

    @Override
    public <T extends MenuItem> T item(int index, Class<T> componentClass) {
        return N2oSelenide.component(elements().get(index), componentClass);
    }

    @Override
    public <T extends MenuItem> T item(Condition condition, Class<T> componentClass) {
        return N2oSelenide.component(elements().findBy(condition), componentClass);
    }
}
