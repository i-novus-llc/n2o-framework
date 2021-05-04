package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.header.SearchBar;
import net.n2oapp.framework.autotest.api.component.page.SearchablePage;
import net.n2oapp.framework.autotest.impl.component.header.N2oSearchBar;

public class N2oSearchablePage extends N2oStandardPage implements SearchablePage {
    @Override
    public SearchBar searchBar() {
        return N2oSelenide.component(element().$(".n2o-search-bar"), N2oSearchBar.class);
    }
}
