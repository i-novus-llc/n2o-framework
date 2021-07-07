package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.header.SearchItem;

/**
 * Список с результатами поиска в шапке для автотестирования
 */
public interface SearchResult extends ComponentsCollection {

    <T extends SearchItem> T item(int index, Class<T> componentClass);

}