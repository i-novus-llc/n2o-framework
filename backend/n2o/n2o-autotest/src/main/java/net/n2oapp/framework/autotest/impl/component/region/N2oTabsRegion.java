package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.*;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;

/**
 * Регион в виде вкладок для автотестирования
 */
public class N2oTabsRegion extends N2oRegion implements TabsRegion {

    private static final String SCROLL_BAR = "tab-content_no-scrollbar";

    @Override
    public TabItem tab(int index) {
        return new N2oTabItem(navItem().shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index));
    }

    @Override
    public void shouldHaveSize(int size) {
        navItem().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldHaveMaxHeight(int height) {
        getTabsContent().shouldBe(Condition.attributeMatching("style", String.format(".*max-height: %dpx;.*", height)));
    }

    @Override
    public void shouldHaveScrollbar() {
        getTabsContent().shouldNotHave(Condition.cssClass(SCROLL_BAR));
    }

    @Override
    public void shouldNotHaveScrollbar() {
        getTabsContent().shouldHave(Condition.cssClass(SCROLL_BAR));
    }

    @Override
    public TabItem tab(Condition by) {
        return new N2oTabItem(navItem().findBy(by));
    }

    public static class N2oTabItem extends N2oComponent implements TabItem {

        public N2oTabItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public RegionItems content() {
            SelenideElement elm = element().parent().parent().parent().parent().$$(".tab-pane")
                    .findBy(Condition.cssClass("active"));

            ElementsCollection nestingElements = elm.$$(".tab-pane.active .tab-pane.active > div > div");
            ElementsCollection firstLevelElements = elm.$$(".tab-pane.active > div > div")
                    .filter(new Condition("shouldBeFirstLevelElement") {
                        @Nonnull
                        @Override
                        public CheckResult check(Driver driver, WebElement element) {
                            boolean result = !nestingElements.contains(element);
                            return new CheckResult(result ? CheckResult.Verdict.ACCEPT : CheckResult.Verdict.REJECT, (Object)null);
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
            element().shouldHave(Condition.cssClass("active"));
        }

        @Override
        public void shouldNotBeActive() {
            element().shouldNotHave(Condition.cssClass("active"));
        }

        @Override
        public void shouldBeInvalid() {
            element().shouldHave(Condition.cssClass("invalid"));
        }

        @Override
        public void shouldBeValid() {
            element().shouldNotHave(Condition.cssClass("invalid"));
        }

        @Override
        public void scrollUp() {
            Selenide.executeJavaScript("document.querySelector('.tab-content_fixed').scrollTop = 0");
        }

        @Override
        public void scrollDown() {
            Selenide.executeJavaScript("document.querySelector('.tab-content_fixed').scrollTop = document.querySelector('.tab-content_fixed').scrollHeight");
        }
    }

    protected SelenideElement getTabsContent() {
        return element().$(".tab-content");
    }

    protected ElementsCollection navItem() {
        return element().$$(".n2o-tabs-nav-item");
    }
}
