package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.collection.SearchResult;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Панель поиска в хедере для автотестирования
 */
public interface SearchBar extends Component {

    void click();

    void search(String title);

    SearchResult searchResult();

    void clear();

}