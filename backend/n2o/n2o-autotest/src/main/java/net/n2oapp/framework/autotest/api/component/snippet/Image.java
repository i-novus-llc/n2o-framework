package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.TextPosition;

/**
 * Компонент Image для автотестирования
 */
public interface Image extends Snippet {

    void shouldHaveTitle(String text);

    void shouldHaveDescription(String text);

    void shouldHaveUrl(String url);

    void shouldHaveWidth(int size);

    void shouldHaveTextPosition(TextPosition align);

}
