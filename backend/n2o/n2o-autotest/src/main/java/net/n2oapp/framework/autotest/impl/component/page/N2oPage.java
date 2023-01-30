package net.n2oapp.framework.autotest.impl.component.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.application.Footer;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import net.n2oapp.framework.autotest.impl.component.application.N2oFooter;
import net.n2oapp.framework.autotest.impl.component.application.N2oSidebar;
import net.n2oapp.framework.autotest.impl.component.header.N2oSimpleHeader;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;

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
        return new N2oPopover(element().$$(".popover .popover-header, .popover-body").findBy(Condition.text(title)).parent());
    }

    @Override
    public Alerts alerts() {
        return N2oSelenide.collection(element().$$(".n2o-alerts-container .n2o-alert"), Alerts.class);
    }

    @Override
    public Alerts alerts(Alert.Placement placement) {
        return N2oSelenide.collection(element().$$(String.format(".n2o-alerts-container .%s .n2o-alert", placement.name())), Alerts.class);
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
        element().click();
        element().sendKeys(Keys.HOME);
    }

    @Override
    public void scrollDown() {
        element().click();
        element().sendKeys(Keys.END);
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

    @Override
    public void shouldHaveError(int statusCode) {
        switch (statusCode) {
            case (404):
                element().shouldHave(Condition.text("404\nСтраница не найдена"));
                break;
            case (500):
                element().shouldHave(Condition.text("500\nВнутренняя ошибка приложения"));
                break;
            case (502):
                element().shouldHave(Condition.text("502\nНеверный ответ от восходящего сервера"));
                break;
            case (403):
                element().shouldHave(Condition.text("403\nДоступ запрещён"));
                break;
            default:
                element().$(".n2o-alert-segment").shouldHave(Condition.text(String.valueOf(statusCode)));
        }
    }

    public class N2oPageToolbar implements PageToolbar {

        @Override
        public Toolbar topLeft() {
            return N2oSelenide.collection(element().$$(".n2o-page-body .n2o-page-actions, .n2o-page-body").first().$$(".btn-toolbar:first-child .btn"), Toolbar.class);
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

        @Deprecated
        @Override
        public void clickLink(String text) {
            element().$$(".n2o-breadcrumb-link").findBy(Condition.text(text)).shouldBe(Condition.exist).click();
        }

        @Deprecated
        @Override
        public void firstTitleShouldHaveText(String text) {
            element().$(".breadcrumb-item").shouldHave(Condition.text(text));
        }

        @Deprecated
        @Override
        public void titleShouldHaveText(String text) {
            element().$$(".breadcrumb-item").last()
                    .shouldHave(Condition.text(text));
        }

        @Deprecated
        @Override
        public void titleByIndexShouldHaveText(String text, Integer index) {
            element().$$(".breadcrumb-item").get(index).shouldHave(Condition.text(text));
        }
        public N2oBreadcrumb(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveSize(int size) {
            element().$$(".breadcrumb-item").should(CollectionCondition.size(size));
        }

        @Override
        public N2oCrumb crumb(int index) {
            return new N2oCrumb(element().$$(".breadcrumb-item").get(index));
        }

        @Override
        public N2oCrumb crumb(String label) {
            return new N2oCrumb(element().$$(".breadcrumb-item").findBy(Condition.text(label)));
        }

        public class N2oCrumb extends N2oComponent implements Crumb {

            public N2oCrumb(SelenideElement element) {
                setElement(element);
            }

            @Override
            public void click() {
                element().click();
            }

            @Override
            public void shouldHaveLabel(String text) {
                element().lastChild().shouldHave(Condition.text(text));
            }

            @Override
            public void shouldHaveLink(String link) {
                element().$(".n2o-breadcrumb-link").shouldHave(Condition.href(link));
            }

            @Override
            public void shouldNotHaveLink() {
                element().$(".n2o-breadcrumb-link").shouldNot(Condition.exist);
            }
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
        public StandardButton button(String label) {
            return N2oSelenide.component(element.$$(".btn").findBy(Condition.text(label)), StandardButton.class);
        }

        @Override
        public void shouldBeClosed(long timeOut) {
            if (element.$(".modal-header .modal-title").exists())
                element.$(".modal-header .modal-title").shouldBe(Condition.exist, Duration.ofMillis(timeOut));
        }

        @Override
        public void shouldHaveReversedButtons() {
            element.$(".btn-group").shouldHave(Condition.cssClass("flex-row-reverse"));
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

    static class UrlMatch extends Condition {
        private final String regex;

        public UrlMatch(String regex) {
            super("urlMatch", true);
            this.regex = regex;
        }

        @Override
        public boolean apply(Driver driver, WebElement element) {
            return driver.url().matches(regex);
        }

        @Override
        public String actualValue(Driver driver, WebElement element) {
            return driver.url();
        }

        @Override
        public String toString() {
            return String.format("%s '%s'", getName(), regex);
        }
    }
}
