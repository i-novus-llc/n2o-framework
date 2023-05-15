package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.component.header.SearchBar;

public interface SearchablePage extends StandardPage {

    /**
     * @return Панель поиска в шапке для автотестирования
     */
    SearchBar searchBar();
}
