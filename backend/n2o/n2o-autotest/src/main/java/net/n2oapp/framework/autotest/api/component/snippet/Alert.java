package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент предупреждения для автотестирования
 */
public interface Alert extends Snippet {
    void shouldHaveColor(Colors color);

    void footerShouldHaveText(String text);

    void headerShouldHaveText(String text);
}
