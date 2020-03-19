package net.n2oapp.framework.autotest.api.component.button;

/**
 * Стандартная кнопка для автотестирования
 */
public interface StandardButton extends Button {
    void click();

    void shouldBeDisabled();
    void shouldBeEnabled();

    void shouldHaveLabel(String label);
}
