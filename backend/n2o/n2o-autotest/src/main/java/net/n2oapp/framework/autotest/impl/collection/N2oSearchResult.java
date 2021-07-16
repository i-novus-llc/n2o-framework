package net.n2oapp.framework.autotest.impl.collection;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.SearchResult;
import net.n2oapp.framework.autotest.api.component.header.SearchItem;

/**
 * Базовый класс списка с результатами поиска в шапке для автотестирования
 */
public class N2oSearchResult extends N2oComponentsCollection implements SearchResult {

    @Override
    public <T extends SearchItem> T item(int index, Class<T> componentClass) {
        return N2oSelenide.component(elements().get(index), componentClass);
    }

}