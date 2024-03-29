package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.header.SearchBar;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.impl.collection.N2oMenu;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Шапка(header) для автотестирования
 */
public class N2oSimpleHeader extends N2oComponent implements SimpleHeader {

    public N2oSimpleHeader(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void shouldHaveBrandName(String brandName, Duration... duration) {
        should(
                Condition.text(brandName),
                brands().filterBy(Condition.not(Condition.cssClass("n2o-brand"))).get(0),
                duration
        );
    }

    @Override
    public Menu nav() {
        return N2oSelenide.collection(element().$$(".main-nav.navbar-nav > li, .main-nav.navbar-nav .n2o-action-item"), N2oMenu.class);
    }

    @Override
    public Menu extra() {
        return N2oSelenide.collection(navbarContainer().$$(".main-nav-extra.navbar-nav > li"), N2oMenu.class);
    }

    @Override
    public SearchBar search() {
        return N2oSelenide.component(navbarContainer().$(".n2o-search-bar"), N2oSearchBar.class);
    }

    @Override
    public void shouldHaveSidebarSwitcher() {
        element().$(".n2o-sidebar-switcher").should(Condition.exist);
    }

    @Override
    public void switchSidebar() {
        element().$(".n2o-sidebar-switcher").click();
    }

    protected SelenideElement navbarContainer() {
        return element().$(".navbar-collapse");
    }

    protected ElementsCollection brands() {
        return element().$$(".navbar-brand");
    }
}
