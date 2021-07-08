package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.SearchResult;
import net.n2oapp.framework.autotest.api.component.header.SearchBar;
import net.n2oapp.framework.autotest.impl.collection.N2oSearchResult;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.Keys;


/**
 * Базовый класс панели поиска в хедере для автотестирования
 */
public class N2oSearchBar extends N2oComponent implements SearchBar {

    @Override
    public void click() {
        element().should(Condition.exist).click();
    }

    @Override
    public void search(String val) {
        input().should(Condition.exist).sendKeys(Keys.chord(Keys.CONTROL, "a"), val);
    }

    @Override
    public void shouldHaveValue(String value) {
        input().shouldHave(Condition.value(value));
    }

    @Override
    public SearchResult searchResult() {
        return N2oSelenide.collection(element().$$(".n2o-search-bar__popup_list .n2o-search-bar__popup_list__item-container")
                , N2oSearchResult.class);
    }

    @Override
    public void clear() {
        element().$(".n2o-search-bar__control .n2o-search-bar__clear-icon").should(Condition.exist).click();
    }

    private SelenideElement input() {
        return element().$(".n2o-search-bar__control .n2o-input-text");
    }
}