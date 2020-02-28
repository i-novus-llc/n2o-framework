package net.n2oapp.framework.autotest.api.component.button;

/**
 * Стандартная кнопка для автотестирования
 */
public interface StandardButton extends Button {

    void shouldBeDisabled();

    void shouldBeEnabled();

    void shouldHaveLabel(String label);
}
