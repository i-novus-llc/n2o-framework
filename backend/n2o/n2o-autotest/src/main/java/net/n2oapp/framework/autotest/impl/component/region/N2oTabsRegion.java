package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.*;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.WebElement;

/**
 * Регион в виде вкладок для автотестирования
 */
public class N2oTabsRegion extends N2oRegion implements TabsRegion {
    @Override
    public TabItem tab(int index) {
        return new N2oTabItem(element().$$(".nav-item").shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index));
    }

    @Override
    public void shouldHaveSize(int size) {
        element().$$(".nav-item").shouldHaveSize(size);
    }

    @Override
    public TabItem tab(Condition by) {
        return new N2oTabItem(element().$$(".nav-item").findBy(by));
    }

    public static class N2oTabItem extends N2oComponent implements TabItem {

        public N2oTabItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public RegionItems content() {
            int index = 0;
            ElementsCollection tabs = element().parent().$$(".nav-item");
            while (!tabs.get(index).is(Condition.text(element().getText()))) index++;

            SelenideElement elm = element().parent().parent().$$(".tab-pane").get(index)
                    .shouldBe(Condition.cssClass("active"));

            ElementsCollection nestingElements = elm.$$(".tab-pane.active .tab-pane.active > div > div");
            ElementsCollection firstLevelElements = elm.$$(".tab-pane.active > div > div")
                    .filter(new Condition("shouldBeFirstLevelElement") {
                        @Override
                        public boolean apply(Driver driver, WebElement element) {
                            return !nestingElements.contains(element);
                        }
                    });
            return N2oSelenide.collection(firstLevelElements, RegionItems.class);
        }

        @Override
        public void click() {
            element().click();
        }

        @Override
        public void shouldHaveName(String text) {
            element().shouldHave(Condition.text(text));
        }

        @Override
        public void shouldNotHaveTitle() {
            element().shouldHave(Condition.exactText(""));
        }

        @Override
        public void shouldBeActive() {
            element().$(".nav-link").shouldHave(Condition.cssClass("active"));
        }

        @Override
        public void shouldNotBeActive() {
            element().$(".nav-link").shouldNotHave(Condition.cssClass("active"));
        }
    }
}
