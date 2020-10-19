package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.header.SearchItem;

/**
 * Поиск в хедере для автотестирования
 */
public interface Search extends ComponentsCollection {

    <T extends SearchItem> T item(int index, Class<T> componentClass);

}