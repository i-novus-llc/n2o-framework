package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.SearchItem;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;


/**
 * Базовый класс элемента выпадающего списка поиска в шапке для автотестирования
 */
public class N2oSearchItem extends N2oComponent implements SearchItem {

    @Override
    public void shouldHaveTitle(String title) {
        element().$(".nav-link").shouldHave(Condition.text(title));
    }

    @Override
    public void shouldHaveDescription(String description) {
        element().$(".nav-link").shouldHave(Condition.attribute("title", description));
    }

    @Override
    public void shouldHaveLink(String url) {
        element().$(".nav-link").shouldHave(Condition.attribute("href", url));
    }


    @Override
    public void shouldHaveIcon(String icon) {
        element().$(".n2o-search-bar__link-container .n2o-search-bar__popup_icon-left").shouldHave(Condition.cssClass(icon));
    }

}