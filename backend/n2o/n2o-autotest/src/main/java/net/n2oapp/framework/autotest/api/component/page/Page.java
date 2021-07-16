package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.component.application.Footer;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;

/**
 * Страница для автотестирования
 */
public interface Page extends Component {

    SimpleHeader header();

    Sidebar sidebar();

    Footer footer();

    PageToolbar toolbar();

    Breadcrumb breadcrumb();

    Dialog dialog(String title);

    Popover popover(String title);

    Tooltip tooltip();

    Alerts alerts();

    void urlShouldMatches(String regexp);

    void titleShouldHaveText(String title);

    void scrollUp();

    void scrollDown();

    void shouldHaveCssClass(String classname);

    void shouldHaveStyle(String style);

    void shouldHaveLayout(NavigationLayout layout);

    interface PageToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }

    interface Breadcrumb extends Component {

        void clickLink(String text);

        void parentTitleShouldHaveText(String text);

        void titleShouldHaveText(String text);
    }

    interface Dialog {
        void shouldBeVisible();

        void shouldHaveText(String text);

        void click(String label);

        void shouldBeClosed(long timeOut);
    }

    interface Popover {
        void shouldBeVisible();

        void shouldHaveText(String text);

        void click(String label);

        void shouldBeClosed(long timeOut);
    }

    interface Tooltip {
        void shouldBeExist();

        void shouldNotBeExist();

        void shouldBeEmpty();

        void shouldHaveText(String... text);
    }

}
