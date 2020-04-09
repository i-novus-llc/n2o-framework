package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент отображения прогресса для автотестирования
 */
public interface Progress extends Snippet {
    void shouldHaveText(String text);

    void shouldHaveValue(Integer value);

    void shouldHaveMax(Integer max);

    void shouldBeAnimated();

    void shouldBeStriped();

    void shouldHaveColor(Colors color);
}
