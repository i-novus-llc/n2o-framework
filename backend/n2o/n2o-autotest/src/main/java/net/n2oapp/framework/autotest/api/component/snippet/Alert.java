package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент предупреждения для автотестирования
 */
public interface Alert extends Snippet {

    void shouldHaveColor(Colors color);

    void shouldHaveTitle(String text);

    void shouldHaveUrl(String url);

    void shouldHaveCloseButton();

    void shouldHavePlacement(Placement placement);

    void shouldHaveStacktrace();

    void shouldHaveTimestamp(String timestamp);

    void shouldHaveTimeout(Integer timeout) throws InterruptedException;

    enum Placement {
        top,
        bottom,
        topLeft,
        topRight,
        bottomLeft,
        bottomRight
    }
}
