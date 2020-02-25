package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Страница для автотестирования
 */
public interface Page extends Component {
    PageToolbar toolbar();

    Breadcrumb breadcrumb();

    Dialog dialog(String title);

    interface PageToolbar {
        Toolbar topLeft();

        Toolbar topRight();

        Toolbar bottomLeft();

        Toolbar bottomRight();
    }

    interface Breadcrumb {
        void activeShouldHaveText(String text);
    }

    interface Dialog {
        void shouldBeVisible();

        void shouldHaveText(String text);

        void click(String label);

        void shouldBeClosed(long timeOut);
    }
}
