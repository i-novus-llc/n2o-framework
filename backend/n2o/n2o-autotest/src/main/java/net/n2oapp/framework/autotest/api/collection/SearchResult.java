package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.header.SearchItem;

/**
 * Список с результатами поиска в шапке для автотестирования
 */
public interface SearchResult extends ComponentsCollection {

    /**
     * <p>
     *     Возвращает элемент выпадающего списка у поиска по индексу
     * </p>
     *
     * <p>For example: {@code
     *     searchResult().item(0, SearchItem.class);
     * }</p>
     *
     * @param index номер элемента
     * @param componentClass возвращаемый тип элемента
     * @return Компонент выпадающего списка поиска в шапке для автотестирования
     */
    <T extends SearchItem> T item(int index, Class<T> componentClass);

}