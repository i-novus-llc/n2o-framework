package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент отображения прогресса для автотестирования
 */
public interface Progress extends Control {
    void shouldHaveText(String text);

    void shouldHaveMax(String max);

    void shouldBeAnimated();

    void shouldBeStriped();

    void shouldHaveColor(Colors color);
}
