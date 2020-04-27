package net.n2oapp.framework.autotest.api.component.button;

import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Стандартная кнопка для автотестирования
 */
public interface StandardButton extends Button, Control {

    void shouldHaveLabel(String label);
}
