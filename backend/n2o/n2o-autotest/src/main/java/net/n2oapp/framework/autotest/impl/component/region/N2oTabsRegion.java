package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.*;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.stream.StreamSupport;

/**
 * Регион в виде вкладок для автотестирования
 */
public class N2oTabsRegion extends N2oRegion implements TabsRegion {

    private static final String SCROLL_BAR = "scrollable";

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
        getTabsContent().shouldHave(Condition.cssClass(SCROLL_BAR));
    }

    @Override
    public void shouldNotHaveScrollbar() {
        getTabsContent().shouldNotHave(Condition.cssClass(SCROLL_BAR));
    }

    @Override
    public TabItem tab(WebElementCondition by) {
        return new N2oTabItem(navItem().findBy(by));
    }

    public static class N2oTabItem extends N2oComponent implements TabItem {

        public N2oTabItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public RegionItems content() {
            return content("nested-content");
        }

        @Override
        public RegionItems content(String className) {
            SelenideElement elm = element().parent().parent().parent().$$(".tabs__content--single")
                    .findBy(Condition.cssClass("active"));

            ElementsCollection nestingElements = elm.$$(".tabs__content--single.active .tabs__content--single.active > div > ." + className);
            ElementsCollection firstLevelElements = elm.$$(".tabs__content--single.active > div > ." + className)
                    .filter(new WebElementCondition("shouldBeFirstLevelElement") {
                        @Nonnull
                        @Override
                        public CheckResult check(Driver driver, WebElement element) {
                            boolean result = StreamSupport.stream(nestingElements.spliterator(), false).noneMatch(element::equals);
                            return new CheckResult(result ? CheckResult.Verdict.ACCEPT : CheckResult.Verdict.REJECT, null);
                        }
                    });
            return N2oSelenide.collection(firstLevelElements, RegionItems.class);
        }

        @Override
        public void click() {
            element().click();
        }

        @Override
        public void shouldHaveName(String text, Duration... duration) {
            should(Condition.text(text), duration);
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
            Selenide.executeJavaScript("document.querySelector('.tabs__content').scrollTop = 0");
        }

        @Override
        public void scrollDown() {
            Selenide.executeJavaScript("document.querySelector('.tabs__content').scrollTop = document.querySelector('.tabs__content').scrollHeight");
        }

        @Override
        public void shouldBeEnabled() {
            element().shouldNotHave(Condition.cssClass("disabled"));
        }

        @Override
        public void shouldBeDisabled() {
            element().shouldHave(Condition.cssClass("disabled"));
        }
    }

    protected SelenideElement getTabsContent() {
        return element().$(".tabs__content");
    }

    protected ElementsCollection navItem() {
        return element().$$(".tabs-nav-item");
    }
}
