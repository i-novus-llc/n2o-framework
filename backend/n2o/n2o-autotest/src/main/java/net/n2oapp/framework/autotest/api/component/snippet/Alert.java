package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент предупреждения для автотестирования
 */
public interface Alert extends Snippet {

    void shouldHaveColor(Colors color);

    void shouldHaveTitle(String text);

    void shouldHaveUrl(String url);

    void shouldHavePlacement(Placement placement);

    void shouldHaveStacktrace();

    void shouldHaveTimestamp(String timestamp);

    void click();

    CloseButton closeButton();

    interface CloseButton extends Component {

        void click();
    }

    enum Placement {
        top,
        bottom,
        topLeft,
        topRight,
        bottomLeft,
        bottomRight
    }
}
