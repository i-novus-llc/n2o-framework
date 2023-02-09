package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент отображения статуса для автотестирования
 */
public interface Status extends Snippet {

    void shouldHaveTextOnLeftPosition();

    void shouldHaveTextOnRightPosition();

    void shouldHaveColor(Colors color);
}
