package net.n2oapp.framework.autotest.impl.component.page;

import com.codeborne.selenide.*;
import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.application.Footer;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import net.n2oapp.framework.autotest.impl.component.application.N2oFooter;
import net.n2oapp.framework.autotest.impl.component.application.N2oSidebar;
import net.n2oapp.framework.autotest.impl.component.header.N2oSimpleHeader;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.time.Duration;

import static com.codeborne.selenide.CheckResult.Verdict.ACCEPT;
import static com.codeborne.selenide.CheckResult.Verdict.REJECT;

/**
 * Страница для автотестирования
 */
public class N2oPage extends N2oComponent implements Page {

    public SimpleHeader header() {
        return new N2oSimpleHeader(element().$(".n2o-header"));
    }

    @Override
    public Sidebar sidebar() {
        return new N2oSidebar(element().$(".n2o-sidebar"));
    }

    @Override
    public Footer footer() {
        return new N2oFooter(element().$(".n2o-footer"));
    }

    @Override
    public PageToolbar toolbar() {
        return new N2oPageToolbar();
    }

    @Override
    public Breadcrumb breadcrumb() {
        return new N2oBreadcrumb(element().$(".breadcrumb"));
    }

    @Override
    public Dialog dialog(String title) {
        return new N2oDialog(element().$$(".modal-dialog").findBy(Condition.text(title)).parent());
    }

    @Override
    public Popover popover(String title) {
        return new N2oPopover(element().$$(".popover .popover-header").findBy(Condition.text(title)).parent());
    }

    @Override
    public Tooltip tooltip() {
        return new N2oTooltip(element().$(".list-text-cell__tooltip-container, .show.tooltip"));
    }

    @Override
    public Alerts alerts() {
        return N2oSelenide.collection(element().$$(".n2o-alerts-container .n2o-alert"), Alerts.class);
    }

    @Override
    public void urlShouldMatches(String regexp) {
        element().should(new UrlMatch(regexp));
    }

    @Override
    public void titleShouldHaveText(String title) {
        element().$(".n2o-page__title").shouldHave(Condition.text(title));
    }

    @Override
    public void scrollUp() {
        Selenide.executeJavaScript("window.scrollTo(0, 0)");
    }

    @Override
    public void scrollDown() {
        Selenide.executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void shouldHaveCssClass(String classname) {
        element().$(".n2o-page-body").shouldHave(Condition.cssClass(classname));
    }

    @Override
    public void shouldHaveStyle(String style) {
        element().$(".n2o-page-body").shouldHave(Condition.attribute("style", style));
    }

    @Override
    public void shouldHaveLayout(NavigationLayout layout) {
        if (NavigationLayout.fullSizeHeader.equals(layout))
            element().$(".n2o-layout-full-size-header").should(Condition.exist);
        else if (NavigationLayout.fullSizeSidebar.equals(layout))
            element().$(".n2o-layout-full-size-sidebar").should(Condition.exist);
    }


    public class N2oPageToolbar implements PageToolbar {

        @Override
        public Toolbar topLeft() {
            return N2oSelenide.collection(element().$$(".n2o-page-body .n2o-page-actions").first().$$(".btn-toolbar:first-child .btn"), Toolbar.class);
        }

        @Override
        public Toolbar topRight() {
            return N2oSelenide.collection(element().$$(".n2o-page-body .n2o-page-actions").first().$$(".btn-toolbar:last-child .btn"), Toolbar.class);
        }

        @Override
        public Toolbar bottomLeft() {
            return N2oSelenide.collection(element().$$(".n2o-page-body .n2o-page-actions").last().$$(".btn-toolbar:first-child .btn"), Toolbar.class);
        }

        @Override
        public Toolbar bottomRight() {
            return N2oSelenide.collection(element().$$(".n2o-page-body .n2o-page-actions").last().$$(".btn-toolbar:last-child .btn"), Toolbar.class);
        }
    }

    public class N2oBreadcrumb extends N2oComponent implements Breadcrumb {

        public N2oBreadcrumb(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void clickLink(String text) {
            element().$$(".n2o-breadcrumb-link").findBy(Condition.text(text)).shouldBe(Condition.exist).click();
        }

        @Override
        public void firstTitleShouldHaveText(String text) {
            element().$(".breadcrumb-item").shouldHave(Condition.text(text));
        }

        @Override
        public void titleShouldHaveText(String text) {
            element().$(".active.breadcrumb-item")
                    .shouldHave(Condition.text(text));
        }

        @Override
        public void titleByIndexShouldHaveText(String text, Integer index) {
            element().$$(".breadcrumb-item").get(index).shouldHave(Condition.text(text));
        }
    }

    public static class N2oDialog implements Dialog {
        private final SelenideElement element;

        public N2oDialog(SelenideElement element) {
            this.element = element;
        }

        @Override
        public void shouldBeVisible() {
            element.shouldBe(Condition.visible);
        }

        @Override
        public void shouldHaveText(String text) {
            element.$(".modal-body").shouldHave(Condition.text(text));
        }

        @Override
        public void click(String label) {
            element.$$(".btn").findBy(Condition.text(label)).click();
        }

        @Override
        public void shouldBeClosed(long timeOut) {
            if (element.$(".modal-header .modal-title").exists())
                element.$(".modal-header .modal-title").shouldBe(Condition.exist, Duration.ofMillis(timeOut));
        }
    }

    public static class N2oPopover implements Popover {
        private final SelenideElement element;

        public N2oPopover(SelenideElement element) {
            this.element = element;
        }

        @Override
        public void shouldBeVisible() {
            element.shouldBe(Condition.visible);
        }

        @Override
        public void shouldHaveText(String text) {
            element.$(".popover-body").shouldHave(Condition.text(text));
        }

        @Override
        public void click(String label) {
            element.$$(".btn").findBy(Condition.text(label)).click();
        }

        @Override
        public void shouldBeClosed(long timeOut) {
            if (element.$(".popover-header .popover-body").exists())
                element.$(".popover-header .popover-body").shouldBe(Condition.exist, Duration.ofMillis(timeOut));
        }
    }

    public static class N2oTooltip implements Tooltip {
        private final SelenideElement element;

        public N2oTooltip(SelenideElement element) {
            this.element = element;
        }

        @Override
        public void shouldBeExist() {
            element.shouldBe(Condition.exist);
        }

        @Override
        public void shouldNotBeExist() {
            element.shouldNotBe(Condition.exist);
        }

        @Override
        public void shouldBeEmpty() {
            element.shouldBe(Condition.empty);
        }

        @Override
        public void shouldHaveText(String... text) {
            ElementsCollection items = element.$$(".list-text-cell__tooltip-container__body, .tooltip-inner");
            items.shouldHave(CollectionCondition.size(text.length));
            if (text.length != 0)
                items.shouldHave(CollectionCondition.texts(text));
        }
    }

    static class UrlMatch extends Condition {
        private final String regex;

        public UrlMatch(String regex) {
            super("urlMatch", true);
            this.regex = regex;
        }


        @Nonnull
        @Override
        public CheckResult check(Driver driver, WebElement element) {
            boolean result = driver.url().matches(regex);
            return new CheckResult(result ? ACCEPT : REJECT, null);
        }

        @Override
        public String toString() {
            return String.format("%s '%s'", getName(), regex);
        }
    }
}
