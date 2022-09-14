package net.n2oapp.framework.autotest.api.component.button;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Tooltip;

/**
 * Стандартная кнопка для автотестирования
 */
public interface StandardButton extends Button {

    void shouldBeDisabled();

    void shouldBeEnabled();

    void shouldHaveLabel(String label);

    void shouldHaveIcon(String name);

    void shouldHaveColor(Colors color);

    void hover();

    Tooltip tooltip();
}
