package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.header.SearchBar;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.impl.collection.N2oMenu;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Шапка(header) для автотестирования
 */
public class N2oSimpleHeader extends N2oComponent implements SimpleHeader {

    public N2oSimpleHeader(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void brandNameShouldBe(String brandName) {
        element().$$(".navbar-brand").filterBy(Condition.not(Condition.cssClass("n2o-brand"))).get(0)
                .shouldHave(Condition.text(brandName));
    }

    @Override
    public Menu nav() {
        return N2oSelenide.collection(element().$$(".navbar-collapse .navbar-nav").get(0).$$("ul > li")
                , N2oMenu.class);
    }

    @Override
    public Menu extra() {
        return N2oSelenide.collection(element().$$(".navbar-collapse .navbar-nav").get(1).$$("ul > li")
                , N2oMenu.class);
    }

    @Override
    public SearchBar search() {
        return N2oSelenide.component(element().$(".navbar-collapse .n2o-search-bar"), N2oSearchBar.class);
    }

    @Override
    public void sidebarSwitcherShouldExist() {
        element().$(".n2o-sidebar-switcher").should(Condition.exist);
    }

    @Override
    public void switchSidebar() {
        element().$(".n2o-sidebar-switcher").click();
    }

}
