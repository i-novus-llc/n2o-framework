package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.component.header.SearchBar;

public interface SearchablePage extends StandardPage {
    SearchBar searchBar();
}
