package net.n2oapp.framework.autotest.impl.collection;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Search;
import net.n2oapp.framework.autotest.api.component.header.SearchItem;

/**
 * Поиск в хедере для автотестирования
 */
public class N2oSearch extends N2oComponentsCollection implements Search {

    @Override
    public <T extends SearchItem> T item(int index, Class<T> componentClass) {
        return N2oSelenide.component(elements().get(index), componentClass);
    }

}