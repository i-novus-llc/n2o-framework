package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

/**
 * Компонент текста для автотестирования
 */
public interface Text extends Snippet {
    void shouldHaveText(String text);
}
