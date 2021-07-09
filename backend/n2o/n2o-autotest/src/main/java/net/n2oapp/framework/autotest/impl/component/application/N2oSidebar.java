package net.n2oapp.framework.autotest.impl.component.application;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
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
    public void brandNameShouldBe(String brandName) {
        element().$$(".navbar-brand").filterBy(Condition.not(Condition.cssClass("n2o-brand"))).get(0)
                .shouldHave(Condition.text(brandName));
    }

    @Override
    public void brandLogoShouldBe(String brandName) {
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

}
