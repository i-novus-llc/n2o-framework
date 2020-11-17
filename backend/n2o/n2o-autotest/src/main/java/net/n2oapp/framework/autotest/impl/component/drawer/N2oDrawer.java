package net.n2oapp.framework.autotest.impl.component.drawer;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.WebElement;

public class N2oDrawer extends N2oComponent implements Drawer {
    @Override
    public <T extends Page> T content(Class<T> pageClass) {
        return N2oSelenide.component(element().$(".drawer-content .n2o-page-body"), pageClass);
    }

    @Override
    public void shouldHaveTitle(String text) {
        element().$(".drawer-title")
                .shouldHave(Condition.text(text));
    }

    @Override
    public void placementShouldBe(Placement placement) {
        element().shouldHave(Condition.cssClass("drawer-" + placement.name()));
    }

    @Override
    public void widthShouldBe(String width) {
        element().$(".drawer-content-wrapper").shouldHave(new StyleAttribute("width", width));
    }

    @Override
    public void heightShouldBe(String height) {
        element().$(".drawer-content-wrapper").shouldHave(new StyleAttribute("height", height));
    }

    @Override
    public void close() {
        element().$(".drawer-handle").click();
    }

    static class StyleAttribute extends Condition {
        private final String attributeName;
        private final String expectedAttributeValue;

        StyleAttribute(String attributeName, String expectedAttributeValue) {
            super("attribute");
            this.attributeName = attributeName;
            this.expectedAttributeValue = expectedAttributeValue;
        }

        @Override
        public boolean apply(Driver driver, WebElement element) {
            return getAttributeValue(element).contains(attributeName + ": " + expectedAttributeValue);
        }

        @Override
        public String actualValue(Driver driver, WebElement element) {
            return String.format("style=\"%s\"", getAttributeValue(element));
        }

        @Override
        public String toString() {
            return String.format("attribute style contains %s: %s", attributeName, expectedAttributeValue);
        }

        private String getAttributeValue(WebElement element) {
            String attr = element.getAttribute("style");
            return attr == null ? "" : attr;
        }
    }

    @Override
    public void shouldHaveFooter() {
        element().$(".drawer-footer").shouldBe(Condition.exist);
    }

    @Override
    public void footerShouldBeFixed() {
        element().$(".drawer-footer").shouldBe(Condition.cssClass("drawer-footer--fixed"));
    }

    @Override
    public void footerShouldNotBeFixed() {
        element().$(".drawer-footer").shouldNotBe(Condition.cssClass("drawer-footer--fixed"));

    }
}
