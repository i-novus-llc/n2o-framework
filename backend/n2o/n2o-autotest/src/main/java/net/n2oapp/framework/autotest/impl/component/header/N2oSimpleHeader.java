package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.collection.Search;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.impl.collection.N2oMenu;
import net.n2oapp.framework.autotest.impl.collection.N2oSearch;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.Keys;

/**
 * Простой хедер для автотестирования
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
    public Search search() {
        element().$(".navbar-collapse .n2o-search-bar").click();
        return N2oSelenide.collection(element().$$(".n2o-search-bar__popup_list .n2o-search-bar__popup_list__item-container")
                , N2oSearch.class);
    }

    @Override
    public Search search(String val) {
        element().$(".navbar-collapse .n2o-search-bar").click();
        element().$(".navbar-collapse .n2o-search-bar__control .n2o-input-text").sendKeys(Keys.chord(Keys.CONTROL, "a"), val);
        return N2oSelenide.collection(element().$$(".n2o-search-bar__popup_list .n2o-search-bar__popup_list__item-container")
                , N2oSearch.class);
    }

}
