package net.n2oapp.framework.autotest.impl.component.page;

import com.codeborne.selenide.*;
import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.application.Footer;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.button.Button;
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
    public void shouldHaveUrlMatches(String regex) {
        element().should(new UrlMatch(regex));
    }

    @Override
    public void shouldHaveTitle(String title) {
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
        body().shouldHave(Condition.cssClass(classname));
    }

    @Override
    public void shouldHaveStyle(String style) {
        body().shouldHave(Condition.attribute("style", style));
    }

    @Override
    public void shouldHaveLayout(NavigationLayout layout) {
        switch (layout) {
            case fullSizeHeader:
                element().$(".n2o-layout-full-size-header").should(Condition.exist);
                break;
            case fullSizeSidebar:
                element().$(".n2o-layout-full-size-sidebar").should(Condition.exist);
                break;
        }
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

        private static final String TOOLBAR = ".toolbar_placement_%s .btn";
        @Override
        public Toolbar topLeft() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "topLeft")), Toolbar.class);
        }

        @Override
        public Toolbar topRight() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "topRight")), Toolbar.class);
        }

        @Override
        public Toolbar bottomLeft() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "bottomLeft")), Toolbar.class);
        }

        @Override
        public Toolbar bottomRight() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "bottomRight")), Toolbar.class);
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
        public void lastTitleShouldHaveText(String title) {
            crumbs().last().shouldHave(Condition.text(title));
        }

        @Deprecated
        @Override
        public void titleShouldHaveText(String title, Integer index) {
            crumbs().get(index).shouldHave(Condition.text(title));
        }

        public N2oBreadcrumb(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveSize(int size) {
            crumbs().should(CollectionCondition.size(size));
        }

        @Override
        public N2oCrumb crumb(int index) {
            return new N2oCrumb(crumbs().get(index));
        }

        @Override
        public N2oCrumb crumb(String label) {
            return new N2oCrumb(crumbs().findBy(Condition.text(label)));
        }

        private ElementsCollection crumbs() {
            return element().$$(".breadcrumb-item");
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
                breadcrumbLink().shouldHave(Condition.href(link));
            }

            @Override
            public void shouldNotHaveLink() {
                breadcrumbLink().shouldNot(Condition.exist);
            }

            protected SelenideElement breadcrumbLink() {
                return element().$(".n2o-breadcrumb-link");
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
            SelenideElement modalTitle = element.$(".modal-header .modal-title");

            if (modalTitle.exists())
                modalTitle.shouldNotBe(Condition.exist, Duration.ofMillis(timeOut));
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
            popoverBody().shouldHave(Condition.text(text));
        }

        @Override
        public Button button(String label) {
            return N2oSelenide.component(element.shouldBe(Condition.exist).$$(".popover-body .btn").findBy(Condition.exactText(label)), StandardButton.class);
        }

        @Override
        public void shouldBeClosed(long timeOut) {
            SelenideElement popoverBody = popoverBody();

            if (popoverBody.exists())
                popoverBody.shouldBe(Condition.exist, Duration.ofMillis(timeOut));
        }

        private SelenideElement popoverBody() {
            return element.$(".popover-body");
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
        public CheckResult check(Driver driver, @Nonnull WebElement element) {
            boolean result = driver.url().matches(regex);
            return new CheckResult(result ? ACCEPT : REJECT, null);
        }

        @Nonnull
        @Override
        public String toString() {
            return String.format("%s '%s'", getName(), regex);
        }

    }

    protected SelenideElement body() {
        return element().$(".n2o-page-body");
    }
}
