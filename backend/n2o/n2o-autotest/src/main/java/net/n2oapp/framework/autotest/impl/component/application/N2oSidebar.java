package net.n2oapp.framework.autotest.impl.component.application;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.impl.collection.N2oMenu;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Боковая панель для автотестирования
 */
public class N2oSidebar extends N2oComponent implements Sidebar {

    public N2oSidebar(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void shouldHaveTitle(String title) {
        navBrands()
                .filterBy(Condition.not(Condition.cssClass("n2o-brand")))
                .get(0)
                .shouldHave(Condition.exactText(title));
    }

    @Override
    public void shouldHaveBrandLogo(String src) {
        element().$(".n2o-brand__image")
                .shouldHave(Condition.attributeMatching("src", ".*" + src));
    }

    @Override
    public void shouldHaveSubtitle(String subtitle) {
        navBrands()
                .filterBy(Condition.not(Condition.cssClass("n2o-brand")))
                .get(1)
                .shouldHave(Condition.exactText(subtitle));
    }

    @Override
    public void shouldBeFixed() {
        element().parent().shouldHave(Condition.cssClass("n2o-layout-fixed"));
    }

    @Override
    public void shouldBeRight() {
        element().parent().shouldHave(Condition.cssClass("flex-row-reverse"));
    }

    @Override
    public void shouldBeOverlay() {
        element().shouldHave(Condition.cssClass("n2o-sidebar-overlay"));
    }

    @Override
    public void shouldHaveState(SidebarState state) {
        element().shouldHave(Condition.cssClass(state.name()));
    }

    @Override
    public void clickToggleBtn() {
        element().$(".n2o-sidebar__toggler").click();
    }

    @Override
    public Menu nav() {
        return N2oSelenide.collection(element()
                .$$(".n2o-sidebar__nav .n2o-sidebar__nav-list>div"), N2oMenu.class);
    }

    @Override
    public Menu extra() {
        return N2oSelenide.collection(element()
                        .$$(".navbar-collapse .navbar-nav")
                        .get(1)
                        .$$("ul > li"), N2oMenu.class);
    }

    protected ElementsCollection navBrands() {
        return element().$$(".navbar-brand");
    }
}
